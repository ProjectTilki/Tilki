package com.kasirgalabs.tilki.utils;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private static volatile FileManager instance = null;
    private final ArrayList<File> fileList = new ArrayList<>();

    public static FileManager getInstance() {
        if(instance == null) {
            synchronized(FileManager.class) {
                if(instance == null) {
                    instance = new FileManager();
                }
            }
        }
        return instance;
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

    private FileManager() {

    }

    public File getValidFile(String fileName) {
        if(isFileNameValid(fileName)) {
            return new File(fileName);
        }
        return new File(generateFileName(fileName));
    }

    public void trackFile(File file) {
        if(fileList.contains(file)) {
            return;
        }
        fileList.add(file);
    }

    public void untrackFile(File file) {
        fileList.remove(file);
    }

    private String generateFileName(String fileName) {
        for(int i = 0; i < 100; i++) {
            String generatedFileName = i + "_" + fileName;
            if(isFileNameValid(generatedFileName)) {
                return generatedFileName;
            }
        }
        return null;
    }

    private boolean isFileNameValid(String fileName) {
        File file = new File(fileName);
        return !file.exists();
    }
}
