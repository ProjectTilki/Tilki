package com.kasirgalabs.tilki.utils;

import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class FileListModel extends DefaultListModel<String> {

    private String errorMessage = "";
    private final ArrayList<File> list = new ArrayList<File>();

    @Override
    public void addElement(String fileName) {
        if(contains(fileName)) {
            return;
        }
        super.addElement(fileName);
    }

    public void addElement(File file) {
        addElement(file.getAbsolutePath());
    }

    @Override
    public boolean removeElement(Object obj) {
        if(!contains(obj)) {
            return true;
        }
        if(obj instanceof File) {
            return super.removeElement(((File) obj).getAbsolutePath());
        }
        if(obj instanceof String) {
            return super.removeElement(obj);
        }
        return false;
    }

    public boolean areAllFilesExists() {
        boolean fileIsNotMissing = true;
        for(int i = 0; i < list.size(); i++) {
            if(!list.get(i).exists()) {
                this.removeElement(list.get(i).getAbsolutePath());
                fileIsNotMissing = false;
            }
        }
        return fileIsNotMissing;
    }

    public String getErrorMessage() {
        String temp = errorMessage;
        errorMessage = "";
        return temp;
    }

    @Override
    public boolean contains(Object elem) {
        if(elem instanceof File) {
            return super.contains(((File) elem).getAbsolutePath());
        }
        if(elem instanceof String) {
            return super.contains(elem);
        }
        return false;
    }
}
