//package com.univ.labs;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Andrey on 04/16/2017.
// */
//public class AMDFileSystem implements IFileSystem {
//    private final IOSystem disk = new IOSystem(FSConfig.blockNum, FSConfig.blockSize);
//    //private DirectoryEntry[] directoryEntries = new DirectoryEntry[FSConfig.fileDescriptorsNumber];
//    private OFTEntry[] OFT;
//    private FileDescriptor[] fileDescriptors;
//
//    public AMDFileSystem() throws Exception {
//        OFT = new OFTEntry[FSConfig.OFTSize];
//        for(int i = 0; i < FSConfig.OFTSize; i++)
//            OFT[i] = new OFTEntry();
//
//        FileDescriptor directory = new FileDescriptor();
//        directory.length = 0;
//
//        saveDirectory(new DirectoryEntry[FSConfig.fileDescriptorsNumber]);
//
//        fileDescriptors = new FileDescriptor[FSConfig.fileDescriptorsNumber];
//        for(int i = 1; i < FSConfig.fileDescriptorsNumber; i++) {
//            fileDescriptors[i] = new FileDescriptor();
//        }
//    }
//
//    @Override
//    public void create(String filename) throws Exception {
//        if(filename.length() > 4) throw new IllegalArgumentException();
//
//        FileDescriptor freeDescriptor = null; // FileDescriptor.getFreeFileDescriptor();
//        if(freeDescriptor == null) throw new Exception("No more free space");
//
//        DirectoryEntry[] directoryEntries = getDirectoryEntries();
//
//        DirectoryEntry freeDirectoryEntry = getFreeEntry(directoryEntries);
//        freeDirectoryEntry.name = filename;
//        freeDirectoryEntry.descriptorIndex = freeDescriptor.index;
//
//        saveDirectory(directoryEntries);
//
////        int freeBlock = BitmapManager.getFreeBlockIndex();
////        freeDescriptor.blocksIndices[0] = freeBlock;
////        freeDescriptor.length = 0;
////        freeDescriptor.saveToDisk();
////        BitmapManager.markBlockAsUsed(freeBlock);
//    }
//
//    @Override
//    public void destroy(String filename) throws Exception {
//        DirectoryEntry[] directoryEntries = getDirectoryEntries();
//
//        int directoryEntry = getEntryIndexByName(directoryEntries, filename);
//        if(directoryEntry == -1) throw new NoSuchFileException(filename);
//
////        FileDescriptor fileDescriptor = FileDescriptor.readFileDescriptorFromDisk(directoryEntries[directoryEntry].descriptorIndex);
////        fileDescriptor.clear();
////        fileDescriptor.saveToDisk();
//
//        directoryEntries[directoryEntry] = null;
//        saveDirectory(directoryEntries);
//    }
//
//    @Override
//    public int open(String filename) throws Exception {
//        DirectoryEntry[] directoryEntries = getDirectoryEntries();
//
//        int directoryEntry = getEntryIndexByName(directoryEntries, filename);
//        if(directoryEntry == -1) throw new NoSuchFileException(filename);
//
//        FileDescriptor fileDescriptor = FileDescriptor.readFileDescriptorFromDisk(directoryEntries[directoryEntry].descriptorIndex);
//        //file descriptor -> file
//        SystemFile systemFile = new SystemFile(fileDescriptor);
//        for (int i = 0; i < OFT.length; i++) {
//            if(OFT[i] == null) {
//                OFT[i] = systemFile;
//                return i;
//            }
//        }
//        return  -1;
//    }
//
//    @Override
//    public void close(int index) throws IllegalArgumentException {
//        OFT[index].writeBufferToDisk();
//        OFT[index].descriptor.saveToDisk();
//        OFT[index] = null;
//    }
//
//    @Override
//    public List<FileEntry> directory() throws Exception  {
//        List<String> list = new ArrayList<>();
//        DirectoryEntry[] directoryEntries = getDirectoryEntries();
//        for (int i = 0; i < directoryEntries.length; i++) {
//            FileDescriptor fileDescriptor = FileDescriptor.readFileDescriptorFromDisk(directoryEntries[i].descriptorIndex);
//            list.add(directoryEntries[i].name + fileDescriptor.length);
//        }
//        return list;
//    }
//
//    @Override
//    public byte[] read(int index, int count) throws Exception {
//        SystemFile systemFile = OFT[index];
//        if(systemFile.descriptor.length < count) throw new Exception("Count out of file size");
//
//        byte[] res = new byte[count];
//        int offset = 0;
//
//        while (offset < count) {
//            int bufferLen = systemFile.getBuffer().length;
//            int bufferPos = systemFile.getPosition() % bufferLen;
//
//            int readLen = Math.min(count - offset, bufferLen - bufferPos);
//            System.arraycopy(systemFile.getBuffer(), bufferPos, res, offset, readLen);
//            offset += readLen;
//            systemFile.increasePosition(readLen);
//        }
//
//        return res;
//    }
//
//    @Override
//    public void write(int index, byte[] src) throws Exception {
//        SystemFile systemFile = OFT[index];
//        systemFile.addToBuffer(src);
//        systemFile.writeBufferToDisk();
//        //int written = 0;
//
//        /*while(written < src.length) {
//            int bufferLen = file.getBuffer().length;
//            int bufferPos = file.getPosition() % bufferLen;
//
//            int canWriteLen = Math.min(src.length - written, bufferLen - bufferPos);
//
//            System.arraycopy(src, written, file.getBuffer(), bufferPos, canWriteLen);
//
//            file.increasePosition(canWriteLen);
//            written += canWriteLen;
//            if(file.getPosition() == bufferLen - 1) {
//                file.writeBufferToDisk();
//                file.increasePosition(1);
//            }
//        }*/
//    }
//
//    @Override
//    public void lseek(int index, int pos) throws Exception {
//        SystemFile systemFile = OFT[index];
//        if (pos > systemFile.descriptor.length || pos < 0) throw new Exception("Position out of bounds");
//        systemFile.setPosition(pos);
//    }
//
//    DirectoryEntry getFreeEntry(DirectoryEntry[] directoryEntries){
//        for (int i = 0; i < directoryEntries.length; i++) {
//            if(directoryEntries[i] == null) {
//                directoryEntries[i] = new DirectoryEntry();
//                return directoryEntries[i];
//            }
//        }
//        return null;
//    }
//
//    int getEntryIndexByName(DirectoryEntry[] directoryEntries, String filename){
//        for (int i = 0; i < directoryEntries.length; i++) {
//            if(directoryEntries[i] != null && directoryEntries[i].name.equals(filename))
//                return i;
//        }
//        return -1;
//    }
//
//    void saveDirectory(DirectoryEntry[] directoryEntries) throws Exception {
//        DirectoryEntry[] directoryEntries;
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutput byteStream = new ObjectOutputStream(byteArrayOutputStream);
//        byteStream.writeObject(directoryEntries);
//        byteStream.flush();
//        byteArrayOutputStream.toByteArray();
//
//        OFT[0].setPosition(0);
//        OFT[0].clearBuffer();
//
//        OFT[0].addToBuffer(byteArrayOutputStream.toByteArray());
//        OFT[0].writeBufferToDisk();
//
//        OFT[0].descriptor.saveToDisk();
//    }
//
//    private DirectoryEntry[] getDirectoryEntries()  throws Exception{
//        SystemFile directory = OFT[0];
//        byte[] directoryData = read(0, directory.descriptor.length);
////dirData to entries
//        ByteArrayInputStream in = new ByteArrayInputStream(directoryData);
//        ObjectInputStream is = new ObjectInputStream(in);
//        return (DirectoryEntry [])is.readObject();
//    }
//}
