package com.univ.labs;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */
public class IllegalShellArgumentException extends Exception{
    public IllegalShellArgumentException() {
    }

    public IllegalShellArgumentException(String argName) {
        super("Illegal argument " + argName);
    }
}
