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

import com.kasirgalabs.tilki.utils.Exam;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class ExamsComboBoxController implements Initializable, Observer {
    @FXML
    private ComboBox<String> comboBox;
    private final ExamManager examManager = ExamManager.getInstance();
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = User.getInstance();
        comboBox.setItems(FXCollections.observableArrayList(new String[]{}));
        comboBox.getSelectionModel().selectedItemProperty().addListener(new SelectedExamListener());
        examManager.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        List<String> examList = examManager.availableExamNames();
        comboBox.setItems(FXCollections.observableArrayList(examList));
        comboBox.getSelectionModel().selectFirst();
    }

    private class SelectedExamListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
            Exam exam = examManager.getExamByName(newValue);
            user.setExam(exam);
        }
    }
}
