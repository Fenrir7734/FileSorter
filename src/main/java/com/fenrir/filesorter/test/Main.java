package com.fenrir.filesorter;

public class Main {
    public static void main(String[] args) throws Exception {
        new Sorter(
                "%(YYYY)-%(MM)%(/)%(DD)-file",
                "%(YYYY)-%(MM)-%(DD)",
                "/home/fenrir/Documents/Test_environment/screenshot",
                "/home/fenrir/Documents/Test_environment/target");
    }
}
