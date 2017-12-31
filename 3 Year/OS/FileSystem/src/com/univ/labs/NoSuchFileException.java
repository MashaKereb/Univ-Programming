package com.univ.labs;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */
public class NoSuchFileException extends Exception {
    public NoSuchFileException() {
    }

    public NoSuchFileException(String fileName) {
        super("The file \"" + fileName + "\" does not exist");
    }
}
