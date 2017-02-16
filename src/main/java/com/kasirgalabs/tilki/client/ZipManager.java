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

import java.util.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;

public final class ZipManager extends Observable {
    private static ZipManager instance;
    private static TilkiService<Void> zip;
    private static String message;
    private static Number progress;
    private static boolean done;

    private ZipManager() {
        zip = new TilkiService<>("Zip");
        zip.messageProperty().addListener(new ZipMessageListener());
        zip.progressProperty().addListener(new ZipProgressListener());
        zip.setOnSucceeded((WorkerStateEvent event) -> {
            done = true;
            instance.setChanged();
            instance.notifyObservers();
        });
    }

    public void zip() {
        if(zip.isRunning()) {
            return;
        }
        zip.reset();
        zip.start();
    }

    public static ZipManager getInstance() {
        if(instance == null) {
            instance = new ZipManager();
        }
        return instance;
    }

    public String getMessage() {
        return message;
    }

    public Number getProgress() {
        return progress;
    }

    public boolean isDone() {
        return done;
    }

    private static class ZipMessageListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
            message = newValue;
            instance.setChanged();
            instance.notifyObservers();
        }
    }

    private static class ZipProgressListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                            Number newValue) {
            progress = newValue;
            instance.setChanged();
            instance.notifyObservers();
        }
    }
}
