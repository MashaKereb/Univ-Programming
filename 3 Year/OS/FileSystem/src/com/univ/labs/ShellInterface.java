package com.univ.labs;

/**
 * Created by Masha Kereb on 24-Apr-17.
 */
 interface ShellInterface {
     void start();
     void help();
     void quit();
    
     void dump();
     void load();

     void list();
    
     void create();
    void destroy();

     void open();
     void close();
    
     void read();
     void write();
    
}
