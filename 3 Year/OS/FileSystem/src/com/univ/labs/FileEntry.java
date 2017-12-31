package com.univ.labs;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */
public class FileEntry {
    final String filename;
    final int size;

    public FileEntry(String filename, int size) {
        this.filename = filename;
        this.size = size;
    }

    public String toString(){
        return "name: " + filename + " size: " + size;
    }
}
