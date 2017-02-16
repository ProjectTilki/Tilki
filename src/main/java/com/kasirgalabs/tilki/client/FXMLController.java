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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FXMLController implements Initializable {
    @FXML
    private Button fileChooserButton;
    @FXML
    private Label listViewLabel;
    @FXML
    private Label passwordFieldLabel;
    @FXML
    private Label timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTexts();
    }

    @FXML
    private void fileChooserButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tilki");
        Window window = fileChooserButton.getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(window);
        if(files == null) {
            return;
        }
        FileManager fileManager = FileManager.getInstance();
        fileManager.trackUserFiles(files);
    }

    private void initTexts() {
        timeLabel.setText("Geçen Süre:");
        listViewLabel.setText("Dosyalar");
        fileChooserButton.setText("Dosya Seç");
        passwordFieldLabel.setText("Gözetmen Şifresi");
    }
}
