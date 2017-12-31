package com.univ.labs;

import java.nio.ByteBuffer;

public class FileDescriptor{
    int length;
    int[] blocksIndices;

    public FileDescriptor() {}

    public FileDescriptor(int length, int[] blocksIndices) {
        this.length = length;
        this.blocksIndices = blocksIndices;
    }

    byte[] toBytes(){
         byte[] fileDescriptorBytes = new byte[FSConfig.fileDescriptorSize];
         ByteBuffer buffer = ByteBuffer.allocate(FSConfig.fileDescriptorSize);
         buffer.putInt(length);
         for (int blockIndex : blocksIndices)
             buffer.putInt(blockIndex);

         buffer.flip();
         buffer.get(fileDescriptorBytes);
         return fileDescriptorBytes;
    }

    static FileDescriptor fromBytes(byte[] descriptorBytes){
        ByteBuffer buffer = ByteBuffer.wrap(descriptorBytes);
        int length = buffer.getInt();
        int[] blockIndices = new int[FSConfig.maxBlockNumberForFile];
        for (int i = 0; i < blockIndices.length; ++i) {
            blockIndices[i] = buffer.getInt();
        }
        return new FileDescriptor(length, blockIndices);
    }
}