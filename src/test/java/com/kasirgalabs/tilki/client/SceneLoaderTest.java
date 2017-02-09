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

import static org.junit.Assert.assertTrue;

import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.Test;

public class SceneLoaderTest extends Application {
    /**
     * Test of setStage and loadScene method, of class SceneLoader.
     */
    @Test
    public void testSetStageAndLoadScene() {
        launch(SceneLoaderTest.class);
        assertTrue(true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoader.setStage(primaryStage);
        File[] files = getFxmlFiles();
        for(File file : files) {
            String fileName = fileNameWithoutFxmlExtension(file);
            SceneLoader.loadScene(fileName);
        }
        Platform.exit();
    }

    private File[] getFxmlFiles() {
        return new File(getClass().getClassLoader().getResource("fxml").getPath()).listFiles();
    }

    private String fileNameWithoutFxmlExtension(File file) {
        return file.getName().substring(0, file.getName().length() - 5);
    }
}
