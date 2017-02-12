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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author rootg
 */
public class LoginController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label welcomeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTexts();
    }

    @FXML
    private void submitButtonOnAction(ActionEvent event) {
        if(areFieldsValid()) {
            changeScene();
        }
    }

    private boolean areFieldsValid() {
        String name = User.getName();
        String surname = User.getSurname();
        String id = User.getId();
        if(name.isEmpty()) {
            errorLabel.setText("Ad kısmı boş.");
            return false;
        }
        if(surname.isEmpty()) {
            errorLabel.setText("Soyad kısmı boş.");
            return false;
        }
        if(id.isEmpty()) {
            errorLabel.setText("Numara kısmı boş.");
            return false;
        }
        return true;
    }

    private void changeScene() {
        SceneLoader.loadScene("ExamSelection");
        ExamSelectionController.getController().getExams();
    }

    private void initTexts() {
        welcomeLabel.setText("Hoş Geldiniz");
        nameLabel.setText("Ad:");
        surnameLabel.setText("Soyad:");
        idLabel.setText("Numara:");
        submitButton.setText("Giriş");
        errorLabel.setText("");
    }
}
