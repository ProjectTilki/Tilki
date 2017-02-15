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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ElapsedTimeLabelController implements Initializable {
    private static long initialTime = 0;
    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialTime = System.currentTimeMillis();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText(updateTime());
            }
        });
        Timeline timer = new Timeline(keyFrame);
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String updateTime() {
        long time = System.currentTimeMillis() - initialTime;
        time /= 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        seconds = addPrefixZero(seconds);
        minutes = addPrefixZero(minutes);
        hours = addPrefixZero(hours);
        return hours + ":" + minutes + ":" + seconds;
    }

    private String addPrefixZero(String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }
}
