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
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class FileListViewController implements Initializable, Observer {
    @FXML
    private ListView<File> listView;
    private FileManager fileManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileManager = FileManager.getInstance();
        fileManager.addObserver(this);
        listView.getSelectionModel().getSelectedItems().addListener(new SelectedFilesListener());
    }

    @Override
    public void update(Observable o, Object arg) {
        List<File> files = fileManager.getTrackedUserFiles();
        if(files == null) {
            return;
        }
        listView.setItems(FXCollections.observableArrayList(files));
    }

    @FXML
    private void onOnDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if(!dragboard.hasFiles()) {
            event.consume();
            return;
        }
        for(File file : dragboard.getFiles()) {
            if(file.isDirectory()) {
                event.consume();
                return;
            }
        }
        event.acceptTransferModes(TransferMode.COPY);
    }

    @FXML
    private void onDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        fileManager.trackUserFiles(dragboard.getFiles());
        event.setDropCompleted(true);
        event.consume();
    }

    private class SelectedFilesListener implements ListChangeListener<File> {
        @Override
        public void onChanged(Change<? extends File> c) {
            if(c.next()) {
                fileManager.setSelectedFiles(listView.getSelectionModel().getSelectedItems());
            }
        }
    }
}
