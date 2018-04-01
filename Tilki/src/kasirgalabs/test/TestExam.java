package kasirgalabs.test;

import static org.junit.Assert.*;

import org.junit.Test;

import kasirgalabs.Exam;

public class TestExam {

	@Test
	public void test() {
		Exam exam = new Exam("Junit", "Test");
		assertEquals(exam.getName(), "Junit");
		assertEquals(exam.getDescription(), "Test");
	}

}
