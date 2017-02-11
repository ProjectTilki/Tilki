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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ExamSelectionController implements Initializable {

    private static ExamSelectionController controller;
    @FXML
    private Label choiceBoxLabel;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Button examDescriptionButton;
    @FXML
    private Label examLabel;
    private TilkiService<Exam[]> examService;
    private Exam[] exams;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordFieldLabel;
    @FXML
    private Label passwordFieldStatusLabel;
    private TilkiService<Boolean> passwordService;
    @FXML
    private Button refreshButton;
    @FXML
    private Button submitButton;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label userExamLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userSurnameLabel;

    public static ExamSelectionController getController() {
        return controller;
    }

    public void getExams() {
        resetFields();
        if(examService.isRunning()) {
            return;
        }
        examService.reset();
        examService.start();
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
        initTexts();
        initIdPasswordFieldPropertyListener();
        initTooltips();
        initComboBoxSelectedItemListener();
        initGetExamsService();
        initKeyVerifyRefactoredService();
    }

    private void changeScene() {
        SceneLoader.loadScene("FXML");
    }

    @FXML
    private void examDescriptionButtonOnAction(ActionEvent event) throws IOException {
        ExamDescriptionStage examDescription = ExamDescriptionStage.getInstance();
        examDescription.updateExam();
        examDescription.show();
    }

    private String findExamDesciption(String examName) {
        if(exams == null) {
            return null;
        }
        for(Exam exam : exams) {
            if(exam.getName().equals(examName)) {
                return exam.getDescription();
            }
        }
        return null;
    }

    private void initComboBoxSelectedItemListener() {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            userExamLabel.getTooltip().setText(newValue);
            User.setExam(new Exam(newValue, findExamDesciption(newValue)));
            if(newValue != null && !newValue.isEmpty()) {
                setFields();
            }
            else {
                resetFields();
            }
            ExamDescriptionStage examDescription = ExamDescriptionStage.getInstance();
            examDescription.updateExam();
        });
    }

    private void initGetExamsService() {
        examService = new TilkiService<>("GetExams");
        examService.stateProperty().addListener((observable, oldValue, newValue) -> {
            connectionStatusLabel.setText("Bağlanıyor.");
            connectionStatusLabel.setId("infoLabel");
            if(newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                connectionStatusLabel.setText("Bağlanamadı.");
                connectionStatusLabel.setId("errorLabel");
            }
            else if(newValue == Worker.State.SUCCEEDED) {
                connectionStatusLabel.setText("Bağlandı.");
                connectionStatusLabel.setId("successLabel");
                exams = examService.getValue();
                ArrayList<String> examList = new ArrayList<>();
                for(Exam exam : exams) {
                    examList.add(exam.getName());
                }
                comboBox.setItems(FXCollections.observableArrayList(examList));
                comboBox.getSelectionModel().selectFirst();
            }
        });
    }

    private void initIdPasswordFieldPropertyListener() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                submitButton.setDisable(false);
                return;
            }
            submitButton.setDisable(true);
        });
    }

    private void initKeyVerifyRefactoredService() {
        passwordService = new TilkiService<>("KeyVerifyRefactored");
        passwordService.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                getExams();
            }
            else if(newValue == Worker.State.SUCCEEDED) {
                if(passwordService.getValue()) {
                    changeScene();
                    return;
                }
                passwordFieldStatusLabel.setText("Şifre yanlış.");
            }
        });
    }

    private void initTexts() {
        nameLabel.setText("Ad: ");
        surnameLabel.setText("Soyad: ");
        idLabel.setText("Numara: ");
        examLabel.setText("Sınav: ");
        userNameLabel.setText(User.getName());
        userSurnameLabel.setText(User.getSurname());
        userIdLabel.setText(User.getId());
        userExamLabel.setText("");
        refreshButton.setText("Yenile");
        connectionStatusLabel.setText("");
        choiceBoxLabel.setText("Sınav Listesi");
        examDescriptionButton.setText("Sınav Açıklaması");
        passwordFieldLabel.setText("Gözetmen Şifresi");
        passwordFieldStatusLabel.setText("");
        submitButton.setText("Sınavı Başlat");
    }

    private void initTooltips() {
        userNameLabel.setTooltip(TooltipHacker.customTooltip(User.getName(), 100));
        userSurnameLabel.setTooltip(TooltipHacker.customTooltip(User.getSurname(), 100));
        userIdLabel.setTooltip(TooltipHacker.customTooltip(User.getId(), 100));
        userExamLabel.setTooltip(TooltipHacker.customTooltip(null, 100));
    }

    @FXML
    private void refreshButtonOnAction(ActionEvent event) {
        getExams();
    }

    private void resetFields() {
        comboBox.setItems(FXCollections.observableArrayList(new String[]{}));
        userExamLabel.setText("");
        userExamLabel.getTooltip().setText("");
        passwordField.clear();
        passwordField.setDisable(true);
        examDescriptionButton.setDisable(true);
    }

    private void setFields() {
        userExamLabel.setText(User.getExam().getName());
        userExamLabel.getTooltip().setText(User.getExam().getName());
        passwordField.setDisable(false);
        examDescriptionButton.setDisable(false);
    }

    @FXML
    private void submitButtonOnAction(ActionEvent event) {
        User.getExam().setKey(passwordField.getText().toCharArray());
        if(passwordService.isRunning()) {
            return;
        }
        passwordService.reset();
        passwordService.start();
    }

}
