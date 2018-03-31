package kasirgalabs.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestFoxServiceThread {

	DataInputStream in;
	HostSimulator hostSimulator = new HostSimulator();
	
	@Test
	public void testFoxServiceThread_CheckIn() {
		String[] s = {"Check in","name","surName","id","exam"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThread_KeyVerify() {
		String[] s = {"Key verify","name","surName","id","exam","instructorKey"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThread_SendingFile() {
		String[] s = {"Sending file"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	public void testFoxServiceThread_ListExams() {
		String[] s = {"List exams"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	public void testFoxServiceThread_SendingErrorLogs() {
		String[] s = {"Sending error logs"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	
	@Test
	public void testCall() {
		try {
            String data = in.readUTF(); // Read the requested operation.
            if(data.equals("Check in")) {
                testCheckInManager();
            }
            else if(data.equals("Key verify")) {
                testKeyVerifyManager();
            }
            else if(data.equals("Sending file")) {
                //testFileManager();
            }
            else if(data.equals("List exams")) {
                //testExamListManager();
            }
            else if(data.equals("Sending error logs")) {
                //testSaveErrorLogs();
            }
        }
        catch(Exception e) {
           
        }
	}

	private void testKeyVerifyManager() {
		 try {
			// Read informations from socket.
			String name = in.readUTF();
			String surname = in.readUTF();
			String id = in.readUTF();
			String exam = in.readUTF();
			String instructorKey = in.readUTF();

			// Create File references.
			File examFileObject = new File(exam);
			File examKeyFileObject = new File(examFileObject, "exam_key.txt");
			// Check if exam folder or "exam_key.txt" file is missing.
			assertEquals((examFileObject.exists()),true);
			assertEquals((examKeyFileObject.exists()),true);
			

			BufferedReader examKeyFile = new BufferedReader(new FileReader(
			        examKeyFileObject));

			File studentFile = new File(examFileObject, id);
			
			assertEquals(!studentFile.exists(),true);
			assertEquals(!studentFile.isDirectory(),true);
			assertEquals(!studentFile.mkdir(),true);

			File logFileObject = new File(studentFile, id + "_logfile.txt");

			PrintWriter logFile = new PrintWriter(
			        new FileOutputStream(logFileObject, true));

			logFile.print(id + " | ");
			logFile.print(name + " ");
			logFile.print(surname + " | ");
			logFile.print(instructorKey + " --> ");

			String lines;
			boolean isAccepted = false;

			// Match keys.
			while((lines = examKeyFile.readLine()) != null && !isAccepted) {
			    if(lines.equals(instructorKey)) { // Case sensitive key matching.
			        logFile.print("Instructor key is accepted. | ");
			        //out.writeUTF("1");
			        isAccepted = true;
			    }
			}

			if(!isAccepted) { // Key is not matched.
			    logFile.print("Instructor key is not accepted. | ");
			    //out.writeUTF("2");
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat(
			        "yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logFile.println(dateFormat.format(date));

			examKeyFile.close();
			logFile.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void testCheckInManager() {
		 // Read informations from socket.
		try {
        String name = in.readUTF();
        String surname = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();

        File examFileObject = new File(exam);
        if(!examFileObject.exists()) { // Exam folder is missing.
            assertEquals((!examFileObject.exists()),true);
        }

        File file = new File(examFileObject, id + "_logfile.txt");
        boolean isCheckedIn = false;
        // if log file is exists, student trying to reconnect else check in.
        assertEquals((file.exists()),true);

        
			PrintWriter logFile = new PrintWriter(new FileOutputStream(file,true));
			
			
			logFile.print(id + " | ");
			logFile.print(name + " ");
			logFile.print(surname + " | ");
			if(isCheckedIn) {
			    logFile.print("--> Reconnected. | ");
			   // out.writeUTF("1");
			}
			else {
			    logFile.print("--> Check in. | ");
			   // out.writeUTF("2");
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat(
			        "yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logFile.println(dateFormat.format(date));

			logFile.close();
			assertEquals(file.exists(), true);
		    file.delete();
		} catch (Exception e) {
			
		}

	}
	
	
	
}
