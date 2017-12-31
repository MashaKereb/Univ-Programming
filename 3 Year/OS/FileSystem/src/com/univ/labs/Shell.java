//package com.univ.labs;
//
//import java.util.Scanner;
//
///**
// * Created by Masha Kereb on 24-Apr-17.
// */
//public class Shell implements ShellInterface{
//    private AMDFileSystem fileSystem;
//    private boolean stop = false;
//
//
//    private static Shell ourInstance = new Shell();
//
//    public static Shell getInstance() {
//        return ourInstance;
//    }
//
//    private Shell() {
//        try {
//            this.fileSystem = new AMDFileSystem();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//    private void processCommand(String commandString) throws Exception {
//        String[] args = commandString.split(" ");
//        if (args.length == 0){
//            return;
//        }
//        String command = args[0];
//        switch (command){
//            case "ls":
//                if (args.length > 1)
//                    throw new IllegalShellArgumentException(args[1]);
//                this.fileSystem.directory();
//        }
//    }
//
//    @Override
//    public void start() {
//        String greeting = "Welcome to AMD File System! \n";
//        Scanner scanner = new Scanner(System.in);
//        System.out.print(greeting);
//        String com;
//        do {
//            com = scanner.next();
//            try {
//                this.processCommand(com);
//            } catch (IllegalShellArgumentException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } while(!this.stop);
//
//    }
//
//    @Override
//    public void help() {
//         String helpString = "commands and arguments: \n" +
//                "op <name>          : opens the named file for reading and writing," +
//                                    " and displays an index value on the screen \n" +
//                "rd <index> <count> : reads the number of bytes specified as <count>" +
//                                    " from the open file <index> and displays them on the screen\n" +
//                "cr <name>          : creates a new file with the specified name\n" +
//                "ls                 : displays the list of files in the current directory and their size in bytes\n" +
//                "dump <name>        : save the filesystem to specified file <name>\n" +
//                "load <name>        : load filesystem from specified file <name>\n" +
//                "help               : displays the listing of available commands\n" +
//                "cl <index>         : close file with specified index\n" +
//                "wt <index> <info>  : write to specified file\n" +
//                "quit               : close session and exit\n";
//
//         System.out.print(helpString);
//
//    }
//
//    @Override
//    public void quit() {
//
//    }
//
//    @Override
//    public void dump() {
//
//    }
//
//    @Override
//    public void load() {
//
//    }
//
//    @Override
//    public void list() {
//
//    }
//
//    @Override
//    public void create() {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public void open() {
//
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void read() {
//
//    }
//
//    @Override
//    public void write() {
//
//    }
//
//}
