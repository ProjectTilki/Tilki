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

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class FXMLController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label timeLabel;
    @FXML
    private Label elapsedTimeLabel;
    @FXML
    private Button examDescriptionButton;
    @FXML
    private Label listViewLabel;
    @FXML
    private ListView<File> listView;
    @FXML
    private Button fileChooserButton;
    @FXML
    private Button removeSelectedButton;
    @FXML
    private Label passwordFieldLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordFieldStatusLabel;
    @FXML
    private Button submitButton;

    private TilkiService<Boolean> passwordService;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTexts();
        initIdPasswordFieldPropertyListener();
        initKeyVerifyRefactoredService();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initTimer();
    }

    @FXML
    private void examDescriptionButtonOnAction(ActionEvent event) {
        ExamDescriptionStage examDescription = ExamDescriptionStage.getInstance();
        examDescription.updateExam();
        examDescription.show();
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

    @FXML
    private void listViewOnDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if(!dragboard.hasFiles()) {
            event.consume();
            return;
        }
        for(File file : dragboard.getFiles()) {
            if(file.isDirectory()) {
                event.consume();
                return;
            }
        }
        event.acceptTransferModes(TransferMode.COPY);
    }

    @FXML
    private void listViewOnDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        addFiles(dragboard.getFiles());
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    private void removeSelectedButtonOnAction(ActionEvent event) {
        listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void fileChooserButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tilki");
        List<File> files = fileChooser.showOpenMultipleDialog(gridPane.getScene().getWindow());
        if(files == null) {
            return;
        }
        addFiles(files);
    }

    private void initTexts() {
        timeLabel.setText("Geçen Süre:");
        examDescriptionButton.setText("Sınav Açıklaması");
        listViewLabel.setText("Dosyalar");
        fileChooserButton.setText("Dosya Seç");
        passwordFieldLabel.setText("Gözetmen Şifresi");
        passwordFieldStatusLabel.setText("");
        submitButton.setText("Sınavı Bitir");
        removeSelectedButton.setText("Seçili Dosyaları Sil");
    }

    private void initIdPasswordFieldPropertyListener() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordFieldStatusLabel.setText("");
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
            passwordFieldStatusLabel.setId("errorLabel");
            if(newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                passwordFieldStatusLabel.setText("Bağlanamadı.");
            } else if(newValue == Worker.State.SUCCEEDED) {
                if(passwordService.getValue()) {
                    passwordFieldStatusLabel.setText("");
                    return;
                }
                passwordFieldStatusLabel.setText("Şifre yanlış.");
            } else {
                passwordFieldStatusLabel.setText("Bağlanıyor.");
                passwordFieldStatusLabel.setId("infoLabel");
            }
        });
    }

    private void addFiles(List<File> files) {
        for(File file : files) {
            if(!listView.getItems().contains(file)) {
                listView.getItems().add(file);
            }
        }
    }

    private void initTimer() {
        TilkiTimer tilkiTimer = TilkiTimer.getInstance();
        tilkiTimer.initTimer();
        tilkiTimer.addObserver((o, arg) -> {
            elapsedTimeLabel.setText((String) arg);
        });
    }
}
