package com.kasirgalabs.tilki.utils;

import java.io.File;
import java.util.ArrayList;

public class FileFinder {

    public static String generateFileName(String fileName) {
        File file = new File(fileName);
        if(!file.exists()) {
            return fileName;
        }

        String generatedFileName;
        for(int i = 0; i < 100; i++) {
            generatedFileName = i + "_" + fileName;
            if(!new File(generatedFileName).exists()) {
                return generatedFileName;
            }
        }
        return null;
    }

    public static ArrayList<String> findGeneratedFileNames(String fileName) {
        ArrayList<String> fileList = new ArrayList<>();
        if(new File(fileName).exists()) {
            fileList.add(fileName);
        }

        String generatedFileName;
        for(int i = 0; i < 100; i++) {
            generatedFileName = i + "_" + fileName;
            if(new File(generatedFileName).exists()) {
                fileList.add(generatedFileName);
            }
        }
        return fileList;
    }
}
