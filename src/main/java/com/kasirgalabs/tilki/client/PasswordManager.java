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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

public final class PasswordManager extends Observable {
    private static PasswordManager instance;
    private static TilkiService<Boolean> passwordService;
    private static boolean correct;
    private static State state;

    private PasswordManager() {
        passwordService = new TilkiService<>("KeyVerifyRefactored");
        passwordService.stateProperty().addListener(new PasswordServiceListener());
    }

    public static PasswordManager getInstance() {
        if(instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }

    public void checkPassword() {
        if(passwordService.isRunning()) {
            return;
        }
        passwordService.reset();
        passwordService.start();
    }

    public State getState() {
        return state;
    }

    public boolean isCorrect() {
        return correct;
    }

    private static class PasswordServiceListener implements ChangeListener<State> {
        @Override
        public void changed(ObservableValue<? extends State> observable, State oldValue,
                            State newValue) {
            state = newValue;
            correct = false;
            if(newValue == Worker.State.SUCCEEDED) {
                correct = passwordService.getValue();
            }
            instance.setChanged();
            instance.notifyObservers();
        }
    }
}
