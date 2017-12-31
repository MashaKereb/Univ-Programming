package com.univ.labs;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */
public class OFTEntry {
    boolean isFree = true;

    byte[] buffer = new byte[FSConfig.blockSize];

    int descriptorIndex = -1;
}
