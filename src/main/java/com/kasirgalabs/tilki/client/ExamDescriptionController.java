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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ExamDescriptionController implements Initializable {

    private static ExamDescriptionController controller;

    @FXML
    private Label examNameLabel;
    @FXML
    private TextArea examDescriptionTextArea;

    public static ExamDescriptionController getController() {
        return controller;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = this;
        examNameLabel.setText("");
        examDescriptionTextArea.setText("");
    }

    public void updateExam() {
        if(User.getExam() == null) {
            return;
        }
        examNameLabel.setText(User.getExam().getName());
        examDescriptionTextArea.setText(User.getExam().getDescription());
    }
}
