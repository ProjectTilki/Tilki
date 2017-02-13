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

public class User extends Observable {
    private static User instance;
    private static Exam exam;
    private static String id = "";
    private static String name = "";
    private static String surname = "";

    private User() {
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        User.exam = exam;
        setChanged();
        notifyObservers();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        User.id = id;
        setChanged();
        notifyObservers();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        User.name = name;
        setChanged();
        notifyObservers();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        User.surname = surname;
        setChanged();
        notifyObservers();
    }
}
