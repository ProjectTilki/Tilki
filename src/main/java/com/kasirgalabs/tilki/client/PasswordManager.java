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
import java.util.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

public class PasswordManager extends Observable {
    private static TilkiService<Boolean> PASSWORD_SERVICE;
    private static PasswordManager instance;
    private static State state;
    private static boolean correct;

    private PasswordManager() {
    }

    public static PasswordManager getInstance() {
        if(instance == null) {
            instance = new PasswordManager();
            PASSWORD_SERVICE = new TilkiService<>("KeyVerifyRefactored");
            PASSWORD_SERVICE.stateProperty().addListener(new PasswordServiceListener());
        }
        return instance;
    }

    public void checkPassword() {
        User user = User.getInstance();
        Exam exam = user.getExam();
        if(exam == null || exam.getKey() == null) {
            correct = false;
            return;
        }
        if(PASSWORD_SERVICE.isRunning()) {
            return;
        }
        PASSWORD_SERVICE.reset();
        PASSWORD_SERVICE.start();
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
                correct = PASSWORD_SERVICE.getValue();
            }
            instance.setChanged();
            instance.notifyObservers();
        }
    }
}
