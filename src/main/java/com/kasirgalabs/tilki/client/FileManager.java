/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.tilki.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public final class FileManager extends Observable {
    private static FileManager instance;
    private static List<File> trackedUserFiles;
    private static List<File> trackedSystemFiles;
    private static List<File> selectedFiles;
    private static List<File> filesToUpload;

    private FileManager() {
        trackedUserFiles = new ArrayList<>();
        trackedSystemFiles = new ArrayList<>();
        filesToUpload = new ArrayList<>();
    }

    public static FileManager getInstance() {
        if(instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void trackUserFiles(List<File> files) {
        for(File file : files) {
            trackUserFile(file);
        }
    }

    public void trackUserFile(File file) {
        if(file.isDirectory() || trackedUserFiles.contains(file)) {
            return;
        }
        trackedUserFiles.add(file);
        setChanged();
        notifyObservers();
    }

    public List<File> getTrackedUserFiles() {
        return trackedUserFiles;
    }

    public void untrackUserFiles(List<File> files) {
        for(File file : files) {
            trackedUserFiles.remove(file);
        }
        setChanged();
        notifyObservers();
    }

    public void untrackUserFile(File file) {
        trackedUserFiles.remove(file);
        setChanged();
        notifyObservers();
    }

    public void setSelectedFiles(List<File> files) {
        selectedFiles = files;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    public String generateFileName(String name) {
        File file = new File(name);
        if(!file.exists()) {
            return name;
        }
        for(int i = 0; i < 100; i++) {
            String temp = i + "_" + name;
            if(!new File(temp).exists()) {
                return temp;
            }
        }
        return name;
    }

    public void addFileForUpload(File file) {
        filesToUpload.add(file);
    }

    public List<File> getFilesForUpload() {
        return filesToUpload;
    }

    public void trackSystemFile(File file) {
        trackedSystemFiles.add(file);
    }

    public List<File> getTrackedSystemFiles() {
        return trackedSystemFiles;
    }
}
