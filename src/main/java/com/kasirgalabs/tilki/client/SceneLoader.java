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
import javafx.stage.Stage;

public class SceneLoader {

    private static Stage primaryStage;

    public static void setStage(Stage primaryStage) {
        SceneLoader.primaryStage = primaryStage;
    }

    public static void loadScene(String fxml) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(SceneLoader.class.getClassLoader().getResource("fxml/" + fxml + ".fxml"));
        } catch(IOException ex) {
            Logger.getLogger(SceneLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(parent);
        primaryStage.close();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
