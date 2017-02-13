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

import java.util.Observable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class TilkiTimer extends Observable {
    private static String elapsedTime = "00:00:00";
    private static long initialTime = 0;
    private static TilkiTimer instance;

    private TilkiTimer() {
    }

    public static TilkiTimer getInstance() {
        if(instance == null) {
            instance = new TilkiTimer();
        }
        return instance;
    }

    public void initTimer() {
        initialTime = System.currentTimeMillis();
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), (event) -> {
            elapsedTime = updateTime();
            setChanged();
            notifyObservers(elapsedTime);
            clearChanged();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String addPrefixZero(String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
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
}
