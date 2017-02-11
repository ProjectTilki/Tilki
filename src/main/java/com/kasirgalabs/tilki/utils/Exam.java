package com.kasirgalabs.tilki.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Exam} class represents exams.
 */
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;
    private char[] key;
    private String name;

    /**
     * Initializes a newly created {@code Exam} object so that it represents
     * an empty character Exam.
     */
    public Exam() {
    }

    /**
     * Creates an exam with empty description and key.
     *
     * @param name Name of the exam.
     */
    public Exam(String name) {
        this.name = name;
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
     * Two exams are considered equal if their names are equal.
     *
     * @param object The reference object with which to compare.
     *
     * @return {@code true} if this object is the same as the object argument; {@code false}
     *         otherwise.
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
        if(this.hashCode() == 0 || this.hashCode() != exam.hashCode()) {
            return false;
        }
        return this.getName().equals(exam.getName());
    }

    /**
     * @return Description of the exam.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * A deep copy of the key will be returned on call.
     *
     * @return Key of the exam. If the key does not exist returns null.
     */
    public char[] getKey() {
        return deepCopy(key);
    }

    /**
     * @param newKey the key to set
     */
    public void setKey(char[] newKey) {
        this.key = deepCopy(newKey);
    }

    /**
     * @return Name of the exam.
     */
    public String getName() {
        return name;
    }

    /**
     * @param newName The name to set
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash
     * tables such as those provided by {@link java.util.HashMap}.
     *
     * @return The hash code of a non-{@code null} argument and 0 for a {@code null} argument
     */
    @Override
    public int hashCode() {
        if(this.name == null) {
            return 0;
        }
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    private char[] deepCopy(char[] original) {
        if(original == null) {
            return null;
        }
        char[] copy = new char[original.length];
        System.arraycopy(original, 0, copy, 0, copy.length);
        return copy;
    }
}
