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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public final class UploadManager extends java.util.Observable {
    private static UploadManager instance;
    private static TilkiService<Void> upload;
    private static String message;
    private static Number progress;

    private UploadManager() {
        upload = new TilkiService<>("Upload");
        upload.messageProperty().addListener(new UploadMessageListener());
        upload.progressProperty().addListener(new UploadProgressListener());
    }

    public void upload() {
        if(upload.isRunning()) {
            return;
        }
        upload.reset();
        upload.start();
    }

    public static UploadManager getInstance() {
        if(instance == null) {
            instance = new UploadManager();
        }
        return instance;
    }

    public String getMessage() {
        return message;
    }

    public Number getProgress() {
        return progress;
    }

    private static class UploadMessageListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
            message = newValue;
            instance.setChanged();
            instance.notifyObservers();
        }
    }

    private static class UploadProgressListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                            Number newValue) {
            progress = newValue;
            instance.setChanged();
            instance.notifyObservers();
        }
    }
}
