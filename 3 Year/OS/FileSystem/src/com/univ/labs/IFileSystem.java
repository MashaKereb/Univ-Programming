package com.univ.labs;

import java.util.List;

/**
 * Created by Andrey on 04/16/2017.
 */
public interface IFileSystem {
    void create(String filename) throws Exception;

    void destroy(String filename) throws Exception;

    int open(String filename) throws Exception;

    void close(int index) throws IllegalArgumentException;

    List<FileEntry> directory() throws Exception;

    byte[] read(int index, int count) throws Exception;

    void write(int index, byte[] src) throws Exception;

    void lseek(int index, int pos) throws Exception;
}
