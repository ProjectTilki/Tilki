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

    private User user = User.getInstance();

    @Before
    public void setUp() {
        user.setName(null);
        user.setSurname(null);
        user.setId(null);
        user.setExam(null);
    }

    /**
     * Test of setExam And getExam method, of class user.
     */
    @Test
    public void testSetExamAndGetExam() {
        assertNull(null, user.getExam());

        user.setExam(new Exam(""));
        assertEquals(new Exam(""), user.getExam());

        user.setExam(new Exam("Exam0"));
        assertNotSame(new Exam("Exam1"), user.getExam());

        user.setExam(new Exam("  1  "));
        assertEquals(new Exam("  1  "), user.getExam());

        user.setExam(new Exam(null, null, null));
        assertNotSame(new Exam(null, null, null), user.getExam());

        user.setExam(new Exam("", ""));
        assertEquals(new Exam("", ""), user.getExam());

        user.setExam(new Exam("NAME", "DESCRIPTION___0"));
        assertEquals(new Exam("NAME", "DESCRIPTION___1"), user.getExam());

        user.setExam(new Exam(null, null, new char[]{'p', 'a', 's', 's'}));
        assertNotSame(new Exam(null, null, new char[]{'p', 'a', 's', 's'}), user.getExam());

        user.setExam(new Exam("NAME", null, new char[]{'p', 'a', 's', 's'}));
        assertEquals(new Exam("NAME", null, new char[]{'p', 'a', 's', 's'}), user.getExam());

        user.setExam(new Exam("__0__", null, null));
        assertNotSame(new Exam("__1__", null, null), user.getExam());

        user.setExam(new Exam(null, "DESCRIPTION", new char[]{'p', 's', 's'}));
        assertNotSame(new Exam(null, "DESCRIPTION", new char[]{'p', 's', 's'}), user.getExam());
    }

    /**
     * Test of setId and getId methods, of class user.
     */
    @Test
    public void testSetIdAndGetId() {
        assertNull(null, user.getId());

        user.setId("1234");
        assertEquals("1234", user.getId());

        user.setId("");
        assertEquals("", user.getId());

        user.setId("ID Without Number");
        assertEquals("ID Without Number", user.getId());

        user.setId("1234567890");
        assertEquals("1234567890", user.getId());

        user.setId("\n\t ");
        assertEquals("\n\t ", user.getId());
    }

    /**
     * Test of setName and getName methods, of class user.
     */
    @Test
    public void testSetNameAndGetName() {
        assertNull(null, user.getName());

        user.setName("Name");
        assertEquals("Name", user.getName());

        user.setName("");
        assertEquals("", user.getName());

        user.setName("Name With Space");
        assertEquals("Name With Space", user.getName());

        user.setName("Name With Trailing Whitespace\n\t ");
        assertEquals("Name With Trailing Whitespace\n\t ", user.getName());

        user.setName("\n\t Whitespace Before Name");
        assertEquals("\n\t Whitespace Before Name", user.getName());

        user.setName("\n\t Whitespace Before and After Name\n\t ");
        assertEquals("\n\t Whitespace Before and After Name\n\t ", user.getName());
    }

    /**
     * Test of setSurname and getSurname methods, of class user.
     */
    @Test
    public void testSetSurnameAndGetSurname() {
        assertNull(null, user.getSurname());

        user.setSurname("Surname");
        assertEquals("Surname", user.getSurname());

        user.setSurname("");
        assertEquals("", user.getSurname());

        user.setSurname("Name With Space");
        assertEquals("Name With Space", user.getSurname());

        user.setSurname("Surname With Trailing Whitespace\n\t ");
        assertEquals("Surname With Trailing Whitespace\n\t ", user.getSurname());

        user.setSurname("\n\t Whitespace Before Surname");
        assertEquals("\n\t Whitespace Before Surname", user.getSurname());

        user.setSurname("\n\t Whitespace Before and After Surname\n\t ");
        assertEquals("\n\t Whitespace Before and After Surname\n\t ", user.getSurname());
    }
}
