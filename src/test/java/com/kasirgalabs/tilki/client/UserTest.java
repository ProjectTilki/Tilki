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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import com.kasirgalabs.tilki.utils.Exam;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    @Before
    public void setUp() {
        User.setName(null);
        User.setSurname(null);
        User.setId(null);
        User.setExam(null);
    }

    /**
     * Test of setExam And getExam method, of class User.
     */
    @Test
    public void testSetExamAndGetExam() {
        assertNull(null, User.getExam());

        User.setExam(new Exam(""));
        assertEquals(new Exam(""), User.getExam());

        User.setExam(new Exam("Exam0"));
        assertNotSame(new Exam("Exam1"), User.getExam());

        User.setExam(new Exam("  1  "));
        assertEquals(new Exam("  1  "), User.getExam());

        User.setExam(new Exam(null, null, null));
        assertNotSame(new Exam(null, null, null), User.getExam());

        User.setExam(new Exam("", ""));
        assertEquals(new Exam("", ""), User.getExam());

        User.setExam(new Exam("NAME", "DESCRIPTION___0"));
        assertEquals(new Exam("NAME", "DESCRIPTION___1"), User.getExam());

        User.setExam(new Exam(null, null, new char[]{'p', 'a', 's', 's'}));
        assertNotSame(new Exam(null, null, new char[]{'p', 'a', 's', 's'}), User.getExam());

        User.setExam(new Exam("NAME", null, new char[]{'p', 'a', 's', 's'}));
        assertEquals(new Exam("NAME", null, new char[]{'p', 'a', 's', 's'}), User.getExam());

        User.setExam(new Exam("__0__", null, null));
        assertNotSame(new Exam("__1__", null, null), User.getExam());

        User.setExam(new Exam(null, "DESCRIPTION", new char[]{'p', 's', 's'}));
        assertNotSame(new Exam(null, "DESCRIPTION", new char[]{'p', 's', 's'}), User.getExam());
    }

    /**
     * Test of setId and getId methods, of class User.
     */
    @Test
    public void testSetIdAndGetId() {
        assertNull(null, User.getId());

        User.setId("1234");
        assertEquals("1234", User.getId());

        User.setId("");
        assertEquals("", User.getId());

        User.setId("ID Without Number");
        assertEquals("ID Without Number", User.getId());

        User.setId("1234567890");
        assertEquals("1234567890", User.getId());

        User.setId("\n\t ");
        assertEquals("\n\t ", User.getId());
    }

    /**
     * Test of setName and getName methods, of class User.
     */
    @Test
    public void testSetNameAndGetName() {
        assertNull(null, User.getName());

        User.setName("Name");
        assertEquals("Name", User.getName());

        User.setName("");
        assertEquals("", User.getName());

        User.setName("Name With Space");
        assertEquals("Name With Space", User.getName());

        User.setName("Name With Trailing Whitespace\n\t ");
        assertEquals("Name With Trailing Whitespace\n\t ", User.getName());

        User.setName("\n\t Whitespace Before Name");
        assertEquals("\n\t Whitespace Before Name", User.getName());

        User.setName("\n\t Whitespace Before and After Name\n\t ");
        assertEquals("\n\t Whitespace Before and After Name\n\t ", User.getName());
    }

    /**
     * Test of setSurname and getSurname methods, of class User.
     */
    @Test
    public void testSetSurnameAndGetSurname() {
        assertNull(null, User.getSurname());

        User.setSurname("Surname");
        assertEquals("Surname", User.getSurname());

        User.setSurname("");
        assertEquals("", User.getSurname());

        User.setSurname("Name With Space");
        assertEquals("Name With Space", User.getSurname());

        User.setSurname("Surname With Trailing Whitespace\n\t ");
        assertEquals("Surname With Trailing Whitespace\n\t ", User.getSurname());

        User.setSurname("\n\t Whitespace Before Surname");
        assertEquals("\n\t Whitespace Before Surname", User.getSurname());

        User.setSurname("\n\t Whitespace Before and After Surname\n\t ");
        assertEquals("\n\t Whitespace Before and After Surname\n\t ", User.getSurname());
    }
}
