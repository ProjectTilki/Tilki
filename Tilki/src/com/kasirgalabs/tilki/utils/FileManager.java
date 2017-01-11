package com.kasirgalabs.tilki.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;

public class FileManager {

    private static volatile FileManager instance = null;
    private final FileListModel fileListModel = new FileListModel();

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
        fileListModel.addElement(file);
    }

    public void trackFile(String fileName) {
        fileListModel.addElement(fileName);

    }

    public void trackFiles(File[] files) {
        for(File file : files) {
            fileListModel.addElement(file);
        }
    }

    public void trackFiles(List<File> files) {
        for(File file : files) {
            fileListModel.addElement(file);
        }
    }

    public void untrackFile(File file) {
        fileListModel.removeElement(file);
    }

    public void untrackFile(String fileName) {
        fileListModel.removeElement(fileName);
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

    public ListModel<String> getFileListModel() {
        return fileListModel;
    }
}
