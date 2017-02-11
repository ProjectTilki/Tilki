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

public final class User {
    private static Exam exam;
    private static String id;
    private static String name;
    private static String surname;

    private User() {
    }

    public static Exam getExam() {
        return exam;
    }

    public static void setExam(Exam newExam) {
        User.exam = newExam;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String newId) {
        User.id = newId;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String newName) {
        User.name = newName;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String newSurname) {
        User.surname = newSurname;
    }
}
