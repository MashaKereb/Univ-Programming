package com.univ.labs;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */

class DirectoryEntry implements java.io.Serializable{
    String name;
    int descriptorIndex;
    int size;

    public DirectoryEntry(String name, int descriptorIndex, int size) {
        this.name = name;
        this.descriptorIndex = descriptorIndex;
        this.size = size;
    }
}

public class Directory{
    ArrayList<DirectoryEntry> entries = new ArrayList<>();

    public Directory(ArrayList<DirectoryEntry> entries) {
        this.entries = entries;
    }

    byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput byteStream = null;
        try {
            byteStream = new ObjectOutputStream(byteArrayOutputStream);
            for (DirectoryEntry de: entries) {
                byteStream.writeObject(de);
            }

            byteStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    ArrayList<FileEntry> listOfEntries(){
        ArrayList<FileEntry> list = new ArrayList<>(entries.size());
        for (DirectoryEntry entry: entries) {
            list.add(new FileEntry(entry.name, entry.size));
        }
        return list;
    }

    static Directory fromBytes(byte[] data){
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArrayList<DirectoryEntry > array = new ArrayList<>();
        try {

            ObjectInputStream is = new ObjectInputStream(in);

            while (true) {
                array.add((DirectoryEntry )is.readObject());
            }
        } catch (EOFException e){}
          catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return new Directory(array);

    }

    public static void main(String[] args) {
        ArrayList<DirectoryEntry> e = new ArrayList<>();
        for(int i = 0 ; i < 10; i++)
            e.add(new DirectoryEntry("name"+i, i, i));
        Directory dir = new Directory(e);

        byte[] b = dir.toBytes();
        System.out.print(b);

        Directory j = Directory.fromBytes(b);
        for(int i = 0 ; i < 10; i++){
            System.out.println(j.entries.get(i).name);
            System.out.println(j.entries.get(i).descriptorIndex);
        }

        System.out.println(b.length);

    }
}
