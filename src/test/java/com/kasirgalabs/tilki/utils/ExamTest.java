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
package com.kasirgalabs.tilki.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;

public class ExamTest {

    /**
     * Test of setName and getName methods, of class Exam.
     */
    @Test
    public void testSetName() {
        Exam exam = new Exam(null);
        assertNull(null, exam.getName());

        exam.setName("");
        assertEquals("", exam.getName());

        exam.setName("Name");
        assertEquals("Name", exam.getName());

        exam.setName("\n\t Name Whitespaces\n\t ");
        assertEquals("\n\t Name Whitespaces\n\t ", exam.getName());
    }

    /**
     * Test of setDescription and getDescription methods, of class Exam.
     */
    @Test
    public void testSetDescription() {
        Exam exam = new Exam(null, null);
        assertNull(null, exam.getName());

        exam.setDescription("");
        assertEquals("", exam.getDescription());

        exam.setDescription("Description");
        assertEquals("Description", exam.getDescription());

        exam.setDescription("\n\t Description Whitespaces\n\t ");
        assertEquals("\n\t Description Whitespaces\n\t ", exam.getDescription());
    }

    /**
     * Test of setKey and getKey methods, of class Exam.
     */
    @Test
    public void testSetKeyAndGetkey() {
        Exam exam = new Exam(null, null, null);
        assertNull(null, exam.getName());

        exam.setKey(new char[]{});
        assertArrayEquals(new char[]{}, exam.getKey());

        exam.setKey(new char[]{'p'});
        assertArrayEquals(new char[]{'p'}, exam.getKey());

        exam.setKey(new char[]{'p', 'a', 's', 's'});
        assertArrayEquals(new char[]{'p', 'a', 's', 's'}, exam.getKey());

        char[] array = "This must not equal".toCharArray();
        exam.setKey(array);
        assertFalse(array == exam.getKey());

        exam.setKey(array);
        array[0] = 'x';
        assertFalse(Arrays.equals(array, exam.getKey()));
    }

    /**
     * Test of equals and hashCode methods, of class Exam.
     */
    @Test
    public void testEqualsAndHashCode() {
        Exam exam = new Exam(null);
        assertFalse(exam.equals(new Exam(null)));

        exam = new Exam("exam");
        assertTrue(exam.equals(new Exam("exam")));

        exam = new Exam("\n\t Exam Name with Whitespaces\n\t ");
        assertTrue(exam.equals(new Exam("\n\t Exam Name with Whitespaces\n\t ")));

        exam = new Exam("");
        assertTrue(exam.equals(new Exam("")));

        exam = new Exam("EXAM", "This must be same.");
        assertTrue(exam.equals(new Exam("EXAM")));

        exam = new Exam("EXAM", "This must be same.", "This one too".toCharArray());
        assertTrue(exam.equals(new Exam("EXAM")));

        exam = new Exam("EXAM", " 0 ", "This one too".toCharArray());
        assertTrue(exam.equals(new Exam("EXAM", "1")));

        exam = new Exam("EXAM");
        assertTrue(exam.equals(new Exam("EXAM", "1", "admin".toCharArray())));

        exam = new Exam(null, "1", "NOT SAME".toCharArray());
        assertFalse(exam.equals(new Exam(null, "1", "NOT SAME".toCharArray())));

        Exam exam1 = new Exam("SAME");
        Exam exam2 = new Exam("SAME");
        assertTrue(exam1.equals(exam1));
        assertTrue(exam1.equals(exam2));
    }
}
