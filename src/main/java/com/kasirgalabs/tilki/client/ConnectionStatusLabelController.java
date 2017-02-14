package com.kasirgalabs.tilki.client;

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
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ConnectionStatusLabelController implements Initializable, Observer {
    @FXML
    private Label label;
    private ExamManager examManager;
    private PasswordManager passwordManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("");
        examManager = ExamManager.getInstance();
        examManager.addObserver(this);
        passwordManager = PasswordManager.getInstance();
        passwordManager.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o.getClass().equals(examManager.getClass())) {
            updateStatus(examManager.getState());
            return;
        }
        updateStatus(passwordManager.getState());
    }

    private void updateStatus(State state) {
        switch(state) {
            case RUNNING:
                label.setText("Bağlanıyor.");
                label.setId("infoLabel");
                break;
            case SUCCEEDED:
                label.setText("Bağlandı.");
                label.setId("successLabel");
                break;
            case CANCELLED:
            case FAILED:
                label.setText("Bağlanamadı.");
                label.setId("errorLabel");
                break;
            default:
                break;
        }
    }

}
