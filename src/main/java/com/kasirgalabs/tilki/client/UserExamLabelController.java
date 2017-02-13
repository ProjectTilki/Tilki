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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class UserExamLabelController implements Initializable, Observer {
    @FXML
    private Label label;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = User.getInstance();
        user.addObserver(this);
        label.setText("");
        label.setTooltip(TilkiTooltip.getCustomTooltip());
    }

    @Override
    public void update(Observable o, Object arg) {
        Exam exam = user.getExam();
        label.getTooltip().setText("");
        label.setText("");
        if(exam == null) {
            return;
        }
        label.getTooltip().setText(exam.getName());
        label.setText(exam.getName());
    }
}
