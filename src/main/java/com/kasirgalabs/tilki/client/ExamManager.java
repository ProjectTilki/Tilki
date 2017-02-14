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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

public final class ExamManager extends Observable {
    private static TilkiService<Exam[]> examService;
    private static Exam[] exams;
    private static ExamManager instance;
    private static State state;

    private ExamManager() {
    }

    public static ExamManager getInstance() {
        if(instance == null) {
            instance = new ExamManager();
            examService = new TilkiService<>("GetExams");
            examService.stateProperty().addListener(new ExamServiceListener());
        }
        return instance;
    }

    public void fetchExams() {
        if(examService.isRunning()) {
            return;
        }
        examService.reset();
        examService.start();
    }

    public Exam getExamByName(String name) {
        for(Exam exam : exams) {
            if(exam.getName().equals(name)) {
                return exam;
            }
        }
        return null;
    }

    public State getState() {
        return state;
    }

    public List<String> availableExamNames() {
        List<String> examList = new ArrayList<>(0);
        for(Exam exam : exams) {
            examList.add(exam.getName());
        }
        return examList;
    }

    public String getDescriptionByName(String name) {
        for(Exam exam : exams) {
            if(exam.getName().equals(name)) {
                return exam.getDescription();
            }
        }
        return null;
    }

    private static class ExamServiceListener implements ChangeListener<State> {
        @Override
        public void changed(ObservableValue<? extends State> observable, State oldValue,
                            State newValue) {
            state = newValue;
            exams = new Exam[0];
            if(newValue == Worker.State.SUCCEEDED) {
                exams = examService.getValue();
            }
            instance.setChanged();
            instance.notifyObservers();
        }
    }
}
