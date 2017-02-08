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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author rootg
 */
public class LoginController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField idTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button submitButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTexts();
        initIdTextFieldPropertyListener();
    }

    @FXML
    private void submitButtonOnAction(ActionEvent event) {
        if(!areFieldsValid()) {
            return;
        }
        userLoggedIn();
        changeScene();
    }

    private boolean areFieldsValid() {
        if(nameTextField.getText().trim().isEmpty()) {
            errorLabel.setText("Ad kısmı boş.");
            return false;
        }
        if(surnameTextField.getText().trim().isEmpty()) {
            errorLabel.setText("Soyad kısmı boş.");
            return false;
        }
        if(idTextField.getText().trim().isEmpty()) {
            errorLabel.setText("Numara kısmı boş.");
            return false;
        }
        return true;
    }

    private void userLoggedIn() {
        User.setName(nameTextField.getText().trim());
        User.setSurname(surnameTextField.getText().trim());
        User.setId(idTextField.getText().trim());
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

    private void initIdTextFieldPropertyListener() {
        idTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!newValue.matches("\\d*")) {
                idTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
