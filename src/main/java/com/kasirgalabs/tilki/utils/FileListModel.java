package com.kasirgalabs.tilki.utils;

import java.io.File;
import javax.swing.DefaultListModel;

public class FileListModel extends DefaultListModel<String> {

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
        return true;
    }

    public String getErrorMessage() {
        return "";
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
