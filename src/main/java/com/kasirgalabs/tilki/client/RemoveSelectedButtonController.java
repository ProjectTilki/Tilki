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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class RemoveSelectedButtonController implements Initializable {
    @FXML
    private Button button;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        button.setText("Seçili Dosyaları Sil");
    }

    @FXML
    private void onAction(ActionEvent event) {
        FileManager fileManager = FileManager.getInstance();
        if(fileManager.getSelectedFiles() == null) {
            return;
        }
        fileManager.untrackFiles(fileManager.getSelectedFiles());
        fileManager.setSelectedFiles(null);
    }
}
