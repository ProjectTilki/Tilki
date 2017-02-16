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

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ProgressMessageTextAreaController implements Initializable, Observer {
    @FXML
    private TextArea textArea;
    @FXML
    private ZipManager zipManager;
    private UploadManager uploadManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textArea.setText("");
        zipManager = ZipManager.getInstance();
        zipManager.addObserver(this);
        uploadManager = UploadManager.getInstance();
        uploadManager.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message;
        if(o.getClass().equals(ZipManager.class)) {
            message = zipManager.getMessage();
        }
        else {
            message = uploadManager.getMessage();
        }
        String currentText = textArea.getText();
        if(message == null || currentText.contains(message)) {
            return;
        }
        textArea.appendText(message + "\n");
    }
}
