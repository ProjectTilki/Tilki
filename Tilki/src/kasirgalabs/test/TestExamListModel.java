package kasirgalabs.test;

import static org.junit.Assert.*;

import org.junit.Test;

import kasirgalabs.Exam;
import kasirgalabs.ExamListModel;

public class TestExamListModel {
		
		 private  Exam[] testExamList = new Exam[2];
		 private  Exam[] testNullExamList = null;
		 
		 private void setExamList() {
			testExamList[0] = new Exam("name_1", "description_1");
	  		testExamList[1] = new Exam("name_2", "description_2");
		 }
		 
	 	@Test
		public void testExamListReturnsTheSize() {
	 		setExamList();
	 		
	 		ExamListModel examListModel = new ExamListModel(testExamList);
			assertEquals(examListModel.getSize(),2);	
	 	}
	 	@Test
		public void testExamListReturnsTheElement() {
	 		setExamList();
	 		
	 		ExamListModel examListModel = new ExamListModel(testExamList);
	 		assertEquals(examListModel.getElementAt(0),testExamList[0].getName());
	 	}
	 	
	 	
	}
