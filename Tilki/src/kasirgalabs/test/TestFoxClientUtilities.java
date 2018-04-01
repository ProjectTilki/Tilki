package kasirgalabs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import kasirgalabs.Exam;
import kasirgalabs.FoxClientUtilities;

public class TestFoxClientUtilities {
	
	@Test
	public void checkInForNull() {
		try {
			FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
			foxClientUtilities.checkIn(null, "surname", "id", "exam");
			foxClientUtilities.checkIn("name", null, "id", "exam");
			foxClientUtilities.checkIn("name", "surname", null, "exam");
			foxClientUtilities.checkIn("name", "surname", "id", null);
			fail();
		} catch (Exception e) {
			assert(true);
		}
	}
	@Test
	public void checkInForDummy() {
		FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
			try {
				int result = foxClientUtilities.checkIn("name", "surname", "id", "exam");
				assertEquals(result, -1);
			} catch (IOException e) {
				System.out.println("Error on checkInForDummy");
			}
	}
	@Test
	public void verifyInstructorKeyForNull() {
		FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
		try {
			int result =foxClientUtilities.verifyInstructorKey(null, "surname", "id", "exam", "instructorKey");
			assertEquals(result, -1);
			result =foxClientUtilities.verifyInstructorKey("name", null, "id", "exam", "instructorKey");
			assertEquals(result, -1);
			result =foxClientUtilities.verifyInstructorKey("name", "surname", null, "exam", "instructorKey");
			assertEquals(result, -1);
			result =foxClientUtilities.verifyInstructorKey("name", "surname", "id", null, "instructorKey");
			assertEquals(result, -1);
			result =foxClientUtilities.verifyInstructorKey("name", "surname", "id", "exam", null);
			assertEquals(result, -1);
		} catch (Exception e) {
			assert(true);
		}
	}
	
	@Test
	public void verifyInstructorKeyForDummy() {
		FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
			try {
				int result = foxClientUtilities.verifyInstructorKey("name", "surname", "id", "exam", "instructorKey");
				assertEquals(result, -1);
			} catch (IOException e) {
				System.out.println("Error on verifyInstructorKeyForDummy");
			}
	}
	
	
	@Test()
	 public void verifyInstructorKeyTest(){
		     // Connect to the host.
		HostSimulator hostSimulator = new HostSimulator();
		String[] s = {"1"};
	    DataInputStream in = hostSimulator.getDataInputStreamFor(s);
	        
			try {
				 String status = in.readUTF(); // Read the status code.
			        if(status != null) {
			            char c = status.charAt(0);
			            assertEquals(Character.getNumericValue(c),1);
			        }
			} catch (Exception e) {
				System.out.println("Error on verifyInstructorKeyTest");
			}finally {
				hostSimulator.closeHostSimulatorResouces();
			}
	        
	    }
	
	
	
	 @Test
		public void availableExamsGettingTest() {
		 FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
			try {
				Exam[] examList = foxClientUtilities.availableExams();
				assertNotEquals(examList.length, 0);
			} catch (Exception e) {
				System.out.println("Error on availableExamsGettingTest");
			}
		}
	
	@Test
	public void createZipFileTest() {
		 FoxClientUtilities foxClientUtilities = new FoxClientUtilities();
		int numberOfFile = 3;
		File[] testFiles = createTestFileList(numberOfFile);
		try {
			String zipFileName = foxClientUtilities.createZipFile(testFiles);
			if(new File(zipFileName).exists()) // File exists
		    {
				File f = new File(zipFileName);
				assertEquals(f.exists(),true);
				f.delete();
				for (int i = 0; i < numberOfFile; i++) {
					f = new File("testFile"+Integer.toString(i)+".txt");
					f.delete();
				}
				 
		    }
		} catch (Exception e) {
			System.out.println("Error on createZipFileTest");
		}
	}
	
	public File[] createTestFileList(int numberOfFile) {
		File[] testFiles = new File[numberOfFile];
		for (int i = 0; i < numberOfFile; i++) {
			String fname = "testFile"+Integer.toString(i)+".txt";
			testFiles[i]= createTestFile(fname);
		}
		return testFiles;
	}
	public File createTestFile(String fileName) {
		BufferedWriter writer = null;
		File testFile = new File(fileName);
		 try {
			writer = new BufferedWriter(new FileWriter(testFile));
			 writer.write("This is a sample text.");
			 writer.close();
		} catch (Exception e) {

		} finally {
            try {
                writer.close();
            } catch (Exception e) {
            	System.out.println("Error on createTestFile");
            }
        }
		return testFile;
	}
	
	
}
