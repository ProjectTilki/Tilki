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

public class ExamSelectionController implements Initializable {
    @FXML
    private Label choiceBoxLabel;
    @FXML
    private Label examLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label passwordFieldLabel;
    @FXML
    private Button refreshButton;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userSurnameLabel;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = User.getInstance();
        initTexts();
        initTooltips();
    }

    private void initTexts() {
        nameLabel.setText("Ad: ");
        surnameLabel.setText("Soyad: ");
        idLabel.setText("Numara: ");
        examLabel.setText("Sınav: ");
        userNameLabel.setText(user.getName());
        userSurnameLabel.setText(user.getSurname());
        userIdLabel.setText(user.getId());
        refreshButton.setText("Yenile");
        choiceBoxLabel.setText("Sınav Listesi");
        passwordFieldLabel.setText("Gözetmen Şifresi");
    }

    private void initTooltips() {
        userNameLabel.setTooltip(TilkiTooltip.getCustomTooltip(user.getName()));
        userSurnameLabel.setTooltip(TilkiTooltip.getCustomTooltip(user.getSurname()));
        userIdLabel.setTooltip(TilkiTooltip.getCustomTooltip(user.getId()));
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        ExamManager examManager = ExamManager.getInstance();
        examManager.fetchExams();
    }
}
