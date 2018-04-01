package kasirgalabs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import kasirgalabs.Exam;

public class TestFoxServiceThread {


	@Test
	public void testFoxServiceThreadCheckIn() {
		String[] s = {"Check in","name","surName","id","exam"};
		HostSimulator hostSimulator = new HostSimulator();
		DataInputStream in =hostSimulator.getDataInputStreamFor(s);
		testCall(in);
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThreadKeyVerify() {
		String[] s = {"Key verify","name","surName","id","exam","instructorKey"};
		HostSimulator hostSimulator = new HostSimulator();
		DataInputStream in =hostSimulator.getDataInputStreamFor(s);
		testCall(in);
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThreadSendingFile() {
		String[] s = {"Sending file","filename","id","exam"};
		HostSimulator hostSimulator = new HostSimulator();
		DataInputStream in =hostSimulator.getDataInputStreamFor(s);
		testCall(in);
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThreadListExams() {
		String[] s = {"List exams"};
		HostSimulator hostSimulator = new HostSimulator();
		DataInputStream in =hostSimulator.getDataInputStreamFor(s);
		testCall(in);
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThreadSendingErrorLogs() {
		String[] s = {"Sending error logs"};
		HostSimulator hostSimulator = new HostSimulator();
		DataInputStream in =hostSimulator.getDataInputStreamFor(s);
		testCall(in);
		hostSimulator.closeHostSimulatorResouces();
	}
	

	public void testCall(DataInputStream dis) {
		DataInputStream in = dis;
		try {
            String data = in.readUTF(); // Read the requested operation.
            if("Check in".equals(data)) {
                testCheckInManager(in);
            }
            else if("Key verify".equals(data)) {
                testKeyVerifyManager(in);
            }
            else if("Sending file".equals(data)) {
                testFileManager(in);
            }
            else if("List exams".equals(data)) {
                testExamListManager();
            }
            else if("Sending error logs".equals(data)) {
                testSaveErrorLogs();
            }
        }
        catch(Exception e) {
           System.out.println("Error on testCall");
        }
	}

	
	
	private void testCheckInManager(DataInputStream dis) {
		DataInputStream in = dis;
		 // Read informations from socket.
		try {
		   String name = in.readUTF();
		   String surname = in.readUTF();
		   String id = in.readUTF();
		   String exam = in.readUTF();
		
		   File examFileObject = new File(exam);
		   if (!examFileObject.exists()) { // Exam folder is missing.
		   	assertFalse(examFileObject.exists());
		   	//create folder to continue testing
		   	examFileObject.mkdir();
		   }
		   
		  
		
		   File file = new File(examFileObject, id + "_logfile.txt");
		   boolean isCheckedIn = false;
		   // if log file is exists, student trying to reconnect else check in.
		   if(file.exists()) {
		       isCheckedIn = true;
		   }else{
		   	assertEquals(file.exists(),false);
		   }
		      	 
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

		    deleteDirectoryOrFile(examFileObject);
	} catch (Exception e) {
		 System.out.println("Error on testCheckInManager");
	}

	}
	
	private void testKeyVerifyManager(DataInputStream dis) {
		DataInputStream in = dis;
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
			assertEquals(examFileObject.exists(),false);
			assertEquals(examKeyFileObject.exists(),false);
			
			//create folder and file to continue testing
			examFileObject.mkdir();
			examKeyFileObject.createNewFile();

			BufferedReader examKeyFile = new BufferedReader(new FileReader(
			        examKeyFileObject));

			File studentFile = new File(examFileObject, id);
			 if(!studentFile.exists() || !studentFile.isDirectory()) {
		            if(!studentFile.mkdir()) {
		                assertEquals(studentFile.mkdir(), false);
		            }
		        }
			

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
			
			assertEquals(logFileObject.exists(), true);
			
			deleteDirectoryOrFile(examFileObject);
		    
			
		} catch (Exception e) {
			 System.out.println("Error on testKeyVerifyManager");
		}
		
	}

	private void testFileManager(DataInputStream dis) {
		DataInputStream in = dis;
        PrintWriter logFile = null;
      //  FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        try {
            String fileName = in.readUTF();
            String id = in.readUTF();
            String exam = in.readUTF();
            
            File examFileObject = new File(exam);
            File studentFile = new File(examFileObject, id);
            if(!examFileObject.exists()) {
            	 assertEquals((examFileObject.exists()),false);
            }
            else {
            	assertEquals((examFileObject.exists()),true);
            }

          //create folder and file to continue testing
			examFileObject.mkdir();
			studentFile.mkdir();
			
            File temp = new File(studentFile, fileName);
            if(temp.exists()) { // File exists, try finding new file name.
                for(int i = 0; i < 100; i++) {
                    temp = new File(studentFile, i + "_" + fileName);
                    if(!temp.exists()) {
                        fileName = i + "_" + fileName;
                        break;
                    }
                }
            }

            if(temp.exists()) // File exists
            {
            	assert(temp.exists());
            }else{
            	 //create folder and file to continue testing
            	temp.createNewFile();
            }
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            int byteCount;
            byte[] data = new byte[1024];
       //     InputStream os_in = socket.getInputStream();
            HostSimulator hostSimulator = new HostSimulator();
            InputStream os_in = hostSimulator.getInputStream("Test String to create inputStream.");
            File incomingFile = new File(studentFile, fileName);
            // Creates a file to be filled.
            fileOut = new FileOutputStream(incomingFile);
            // Read file data from the socket and write it to a created file.
            while((byteCount = os_in.read(data)) > 0) {
                fileOut.write(data, 0, byteCount);
                md.update(data, 0, byteCount);
            }
            
            if (incomingFile.length()>0){
            	assert(true);
            }else{
            	assert(false);
            }
            
            logFile = new PrintWriter(new FileOutputStream(new File(studentFile,
                    id + "_logfile.txt"), true));

            logFile.print(id + " | ");
            logFile.print("File is received. | ");
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            logFile.println(dateFormat.format(date));

            byte[] rawChecksum = md.digest();
            StringBuilder md5hex = new StringBuilder();
            for(int i = 0; i < rawChecksum.length; i++) {
                md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100,
                        16).substring(1));
            }
//            out.writeUTF(md5hex.toString()); // Send MD5 checksum to the client.
//            out.flush();
            if (md5hex.toString().length()>0){
            	assert(true);
            }else{
            	assert(false);
            }
            logFile.close();
            fileOut.close();
            deleteDirectoryOrFile(examFileObject);
        }catch (Exception e){
        	 System.out.println("Error on testFileManager");
        }
    }

	private void testExamListManager()  throws IOException {
//    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        if(!new File("exam_list.txt").exists()) { // No exams are available.
//            oos.writeObject(null);
//            oos.flush();
//            return;
        	assertEquals(!new File("exam_list.txt").exists(), true);
        }

        //create folder and file to continue testing
        PrintWriter printWriter = new PrintWriter ("exam_list.txt");
        printWriter.println ("exam_1");
        printWriter.println ("exam_2");
        printWriter.close (); 
        File f = new File("exam_1"); f.mkdir();
        f = new File("exam_2");      f.mkdir();
       // exam_1/exam_description.txt not created for null testing
        printWriter = new PrintWriter ("exam_2/exam_description.txt");
        printWriter.println ("exam_2 exam_description");
        printWriter.close ();
        
        BufferedReader fileIn = new BufferedReader(new FileReader(
                "exam_list.txt"));
        // Array list of Exam object contains available exams.
        ArrayList<Exam> examList = new ArrayList<Exam>();
        String exam;
        while((exam = fileIn.readLine()) != null) { // Read exam list.
            if(new File(exam).exists()) {
                // Look for the descripton.
                if(!new File(exam, "exam_description.txt").exists()) {
                    examList.add(new Exam(exam, null));
                   
                    if (exam.equals("exam_1")){
                    	assert(true);
                    }
                    if (exam.equals("exam_2")){
                    	assert(false);
                    }
                  
                    continue;
                }
                
                
                BufferedReader description = new BufferedReader(new FileReader(
                        new File(exam, "exam_description.txt")));
                String examDescription = "";
                String temp;
                boolean firstLine = true;
                // Description is available.
                while((temp = description.readLine()) != null) {
                    if(firstLine) {
                        examDescription += temp;
                        firstLine = false;
                        continue;
                    }
                    examDescription += "\n" + temp;
                }
                examList.add(new Exam(exam, examDescription));
                description.close();
      
               	assertEquals(examList.size(), 2);

            }
        }
        fileIn.close();

        Exam[] exams = new Exam[examList.size()];
        // Convert array list to array.
        for(int i = 0; i < examList.size(); i++) {
            exams[i] = examList.get(i);
        }
//        oos.writeObject(exams); // Send exam list to the client.
//        oos.flush();
        
        deleteDirectoryOrFile("exam_list.txt");
        deleteDirectoryOrFile("exam_1");
        deleteDirectoryOrFile("exam_2");
    }
	
	private void testSaveErrorLogs() throws IOException {
        BufferedWriter fileOut = null;
        try {
        	//int lineCount = in.readInt();
        	
        	//Create data for testing
        	String[] s = {"line1","line2","line3"};
        	HostSimulator hostSimulator = new HostSimulator();
        	DataInputStream in =hostSimulator.getDataInputStreamFor(s);
    		int lineCount = 3;
    		
            File f = new File("clientErrors.log");
            assertEquals(f.exists(), false);
            
            //create folder and file to continue testing
            f.createNewFile();
            
            fileOut = new BufferedWriter(new FileWriter(f, true));
            
            String line;
            while(lineCount > 0) {
                line = in.readUTF();
                fileOut.write(line + "\n");
                lineCount--;
            }
            
        }
        finally {
            if(fileOut != null) {
                fileOut.close();
            }
            File f = new File("clientErrors.log");
            assertNotEquals(f.length(), 0);
        }
        deleteDirectoryOrFile("clientErrors.log");
    }
//////////////////////////////////////////////////////////////////////	
	public static void deleteDirectoryOrFile(String str) {
		File f = new File(str);
		deleteDirectoryOrFile(f);
		
	}
	public static void deleteDirectoryOrFile(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	           deleteDirectoryOrFile(new File(dir, children[i]));
	        }
	    }
	    // The directory is now empty so delete it
	    dir.delete();
	}
}
