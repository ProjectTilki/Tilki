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
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ExamPasswordStatusLabelController implements Initializable, Observer {
    @FXML
    private Label label;
    private PasswordManager passwordManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("");
        passwordManager = PasswordManager.getInstance();
        passwordManager.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        label.setText("");
        if(passwordManager.getState() == Worker.State.SUCCEEDED && !passwordManager.isCorrect()) {
            label.setText("Şifre yanlış!");
        }
    }
}
