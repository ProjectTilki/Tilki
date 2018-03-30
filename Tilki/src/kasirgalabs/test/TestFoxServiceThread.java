package kasirgalabs.test;

import java.io.DataInputStream;

import org.junit.Test;

public class TestFoxServiceThread {

	DataInputStream in;
	HostSimulator hostSimulator = new HostSimulator();
	
	@Test
	public void testFoxServiceThread_CheckIn() {
		String[] s = {"Check in"};
		in =hostSimulator.getDataInputStreamFor(s);
		testCall();
		hostSimulator.closeHostSimulatorResouces();
	}
	@Test
	public void testFoxServiceThread_KeyVerify() {
		String[] s = {"Key verify"};
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
//            else if(data.equals("Key verify")) {
//                testKeyVerifyManager();
//            }
//            else if(data.equals("Sending file")) {
//                testFileManager();
//            }
//            else if(data.equals("List exams")) {
//                testExamListManager();
//            }
//            else if(data.equals("Sending error logs")) {
//                testSaveErrorLogs();
//            }
        }
        catch(Exception e) {
           
        }
	}
	private void testCheckInManager() {
		
		
	}
	
	
	
}
