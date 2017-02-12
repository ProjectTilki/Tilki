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
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author rootg
 */
public class ConnectionStatusLabelController implements Initializable, EventHandler<WorkerStateEvent> {
    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("");
    }

    @Override
    public void handle(WorkerStateEvent event) {
        State state = event.getSource().getState();
        if(state == Worker.State.RUNNING) {
            label.setText("Bağlanıyor.");
            label.setId("errorLabel");
        }
        else if(state == Worker.State.SUCCEEDED) {
            label.setText("Bağlandı.");
            label.setId("successLabel");
        }
        else if(state == Worker.State.CANCELLED || state == Worker.State.FAILED) {
            label.setText("Bağlanamadı.");
            label.setId("errorLabel");
        }
    }

}
