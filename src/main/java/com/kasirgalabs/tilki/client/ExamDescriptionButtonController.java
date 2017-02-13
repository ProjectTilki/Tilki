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
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ExamDescriptionButtonController implements Initializable, Observer {
    @FXML
    private Button button;
    private User user;
    private ExamDescriptionStage examDescription;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = User.getInstance();
        button.setText("Sınav Açıklaması");
        user.addObserver(this);
        examDescription = ExamDescriptionStage.getInstance();

    }

    @Override
    public void update(Observable o, Object arg) {
        button.setDisable(false);
        Exam exam = user.getExam();
        if(exam == null) {
            button.setDisable(true);
        }
    }

    @FXML
    private void onAction(ActionEvent event) {
        examDescription.show();
    }
}
