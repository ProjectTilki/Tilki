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
    private static List<File> trackedFiles;
    private static List<File> selectedFiles;

    private FileManager() {
        trackedFiles = new ArrayList<>();
    }

    public static FileManager getInstance() {
        if(instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void trackFiles(List<File> files) {
        for(File file : files) {
            trackFile(file);
        }
    }

    public void trackFile(File file) {
        if(file.isDirectory() || trackedFiles.contains(file)) {
            return;
        }
        trackedFiles.add(file);
        setChanged();
        notifyObservers();
    }

    public List<File> getTrackedFiles() {
        return trackedFiles;
    }

    public void untrackFiles(List<File> files) {
        for(File file : files) {
            trackedFiles.remove(file);
        }
        setChanged();
        notifyObservers();
    }

    public void untrackFile(File file) {
        trackedFiles.remove(file);
        setChanged();
        notifyObservers();
    }

    public void setSelectedFiles(List<File> files) {
        selectedFiles = files;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}
