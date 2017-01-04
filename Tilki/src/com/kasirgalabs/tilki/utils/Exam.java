package com.kasirgalabs.tilki.utils;

import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Exam} class represents exams.
 */
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String description;
    private final char[] key;

    private Exam() {
        name = null;
        description = null;
        key = null;
    }

    public Exam(String name, String description) {
        this.name = name;
        this.description = description;
        this.key = null;
    }

    public Exam(String name, String description, @NotNull char[] key) {
        this.name = name;
        this.description = description;
        this.key = key;
    }

    /**
     * @return Name of the exam.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Description of the exam.
     */
    public String getDescription() {
        return description;
    }

    public char[] getKey() {
        char[] temp = new char[key.length];
        System.arraycopy(key, 0, temp, 0, temp.length);
        return temp;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object == null) {
            return false;
        }
        if(this.getClass() != object.getClass()) {
            return false;
        }
        Exam exam = (Exam) object;
        return this.getName().equals(exam.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
