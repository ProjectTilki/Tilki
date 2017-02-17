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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public final class SceneLoader {
    private static Stage primaryStage;

    private SceneLoader() {
    }

    public static void setStage(Stage newPrimaryStage) {
        SceneLoader.primaryStage = newPrimaryStage;
    }

    public static void loadScene(String fxml) {
        deleteObservers();
        Parent root;
        try {
            ClassLoader classLoader = SceneLoader.class.getClassLoader();
            FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("fxml/" + fxml + ".fxml"));
            root = (Parent) fxmlLoader.load();
        } catch(IOException ex) {
            Logger.getLogger(SceneLoader.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Scene scene = getValidScene(root);
        primaryStage.close();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void deleteObservers() {
        ExamManager examManager = ExamManager.getInstance();
        examManager.deleteObservers();
        PasswordManager passwordManager = PasswordManager.getInstance();
        passwordManager.deleteObservers();
    }

    private static Scene getValidScene(Parent parent) {
        Class parentClass = parent.getClass();
        if(parentClass.equals(Label.class)) {
            Label label = (Label) parent;
            if(label.getText().isEmpty()) {
                return new Scene(parent, 100, 100);
            }
        }
        return new Scene(parent);
    }
}
