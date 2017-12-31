package com.univ.labs;

/**
 * Created by Andrey on 04/16/2017.
 */
public class SystemFile {
    private IOSystem disk = new IOSystem(FSConfig.blockNum, FSConfig.blockSize);

    public FileDescriptor descriptor;
    private byte[] buffer;
    private int position = 0;

    public SystemFile(FileDescriptor descriptor) {
        if(descriptor.length == -1 || descriptor.blocksIndices[0] == -1) throw new IllegalArgumentException();

        this.descriptor = descriptor;
        buffer = disk.readBlock(descriptor.blocksIndices[0]);
        clearBuffer();
    }

    public void clearBuffer(){
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = -1;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int value) throws Exception {
        int blockIndex = value / buffer.length;

        if (blockIndex < FSConfig.maxBlockNumberForFile - 1 && blockIndex >= 0 && descriptor.blocksIndices[blockIndex] != -1 ) {
            buffer = disk.readBlock(descriptor.blocksIndices[blockIndex]);
            position = value;
        } else throw new Exception("File reading failed");
    }

    public void increasePosition(int value) throws Exception {
        setPosition(position + value);
    }

    public void moveToTheNextPosition() throws Exception {
        increasePosition(1);
    }

    public void writeBufferToDisk(){
        int count = usedByteCount();
        //boolean isWritten = false;
        //for (int i = 0; i < FSConfig.maxBlockNumberForFile; i++) {
            //if (descriptor.blocksIndices[i] == -1) {
                //int freeBlock = BitmapManager.getFreeBlockIndex();
                //descriptor.blocksIndices[i] = freeBlock;
                disk.writeBlock(position / buffer.length, buffer);
                //BitmapManager.markBlockAsUsed(freeBlock);
                descriptor.length = (position / buffer.length) * buffer.length + count;
                position = descriptor.length;
                //isWritten = true;
                //break;
            //}
        //}
        //if (!isWritten) throw new Exception("File is too big");
    }

    public byte[] getBuffer(){
        return buffer;
    }

//    public void addToBuffer(byte[] data){
//        int posToAdd = position % buffer.length;
//        for (int i = 0; i < data.length; i++, posToAdd ++) {
//            if(posToAdd >= buffer.length){
//                writeBufferToDisk();
//
//                int freeBlock = BitmapManager.getFreeBlockIndex();
//                descriptor.blocksIndices[position / buffer.length] = freeBlock;
//                BitmapManager.markBlockAsUsed(freeBlock);
//
//                posToAdd = 0;
//                clearBuffer();
//            }
//            buffer[posToAdd] = data[i];
//        }
//        position = (position / buffer.length) * buffer.length + posToAdd;
//    }

    private int usedByteCount(){
        for (int i = 0; i < buffer.length; i++) {
            if(buffer[i] == -1)
                return i;
        }
        return buffer.length;
    }
}