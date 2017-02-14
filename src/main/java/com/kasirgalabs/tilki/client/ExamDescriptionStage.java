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

public class ExamDescriptionStage extends Stage {
    private static ExamDescriptionStage instance = null;

    private ExamDescriptionStage() {
        this.setTitle("Tilki");
        this.setX(0);
        this.setY(0);
        Parent examDescription = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            examDescription = FXMLLoader.load(classLoader.getResource("fxml/ExamDescription.fxml"));
        } catch(IOException ex) {
            Logger.getLogger(ExamDescriptionStage.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(examDescription, 250, 250);
        this.setScene(scene);
    }

    public static ExamDescriptionStage getInstance() {
        if(instance == null) {
            instance = new ExamDescriptionStage();
        }
        return instance;
    }
}
