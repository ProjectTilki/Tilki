package com.kasirgalabs.tilki.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Exam} class represents exams.
 */
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private char[] key;

    /**
     * Creates an exam with empty description and key.
     *
     * @param name Name of the exam.
     */
    public Exam(String name) {
        this.name = name;
        this.description = null;
        this.key = null;
    }

    /**
     * Creates an exam with empty key.
     *
     * @param name        Name of the exam.
     * @param description Description of the exam.
     */
    public Exam(String name, String description) {
        this.name = name;
        this.description = description;
        this.key = null;
    }

    /**
     * Creates an exam with the given fields.
     *
     * @param name        Name of the exam.
     * @param description Description of the exam.
     * @param key         Key of the exam.
     */
    public Exam(String name, String description, char[] key) {
        this.name = name;
        this.description = description;
        this.key = deepCopy(key);
    }

    /**
     * @param key the key to set
     */
    public void setKey(char[] key) {
        this.key = deepCopy(key);
    }

    /**
     * A deep copy of the key will be returned on call.
     *
     * @return Key of the exam. If the key does not exist returns null.
     */
    public char[] getKey() {
        if(key == null) {
            return null;
        }
        return deepCopy(key);
    }

    /**
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Name of the exam.
     */
    public String getName() {
        return name;
    }

    /**
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Description of the exam.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Two exams are considered equal if their exam names are same.
     *
     * @param object The reference object with which to compare.
     *
     * @return {@code true} if this object is the same as the object argument;
     *         {@code false} otherwise.
     */
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

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    private char[] deepCopy(char[] key) {
        char[] copy = new char[key.length];
        System.arraycopy(key, 0, copy, 0, copy.length);
        return copy;
    }
}
