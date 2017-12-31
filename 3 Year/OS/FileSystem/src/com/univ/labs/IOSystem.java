package com.univ.labs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOSystem {
    private byte[][] logicDisk;

    private int blockSize;
    private int blockNum;

    IOSystem(int blockNum, int blockSize) {
        this.blockNum = blockNum;
        this.blockSize = blockSize;
        logicDisk = new byte[blockNum][blockSize];

        for (int i = 0; i < this.logicDisk.length; i++) {
            for (int j = 0; j < this.logicDisk[i].length; j++) {
                this.logicDisk[i][j] = -1;
            }
        }
    }

    public byte[] readBlock(int index) {
        if (index >= blockNum || index < 0) throw new IllegalArgumentException();
        return logicDisk[index].clone();
    }

    public void writeBlock(int index, byte[] buffer) {
        if (index >= blockNum || index < 0) throw new IllegalArgumentException();

        for (int i = 0; i < logicDisk[index].length; i++) {
            if (i < buffer.length)
                logicDisk[index][i] = buffer[i];
            else
                logicDisk[index][i] = -1;
        }
    }

    public void readDiskFromFile(File file) throws IOException, FileReadingException {
        FileInputStream scanner = new FileInputStream(file);

        byte[] block = new byte[4];
        scanner.read(block);
        int blNum = Converter.intFromByteArray(block);

        scanner.read(block);
        int blsize = Converter.intFromByteArray(block);

        byte[][] newLogicDisk = new byte[blNum][blsize];
        for (int i = 0; i < blNum; i++) {
            scanner.read(newLogicDisk[i]);

        }
        scanner.close();

        logicDisk = newLogicDisk;
        blockNum = blNum;
        blockSize = blsize;

    }

    public void writeDiskToFile(File file) throws FileWritingException {
        try {
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(Converter.intToByteArray(this.blockNum));
            writer.write(Converter.intToByteArray(this.blockSize));
            for (int i = 0; i < this.blockNum; i++)
                writer.write(this.logicDisk[i]);
        } catch (IOException e) {
            throw new FileWritingException("File writing error");
        }
    }
}
