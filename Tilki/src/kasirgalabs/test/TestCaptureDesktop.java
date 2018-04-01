package kasirgalabs.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import kasirgalabs.CaptureDesktop;

public class TestCaptureDesktop {


	@Before
	public void initialize () {
		CaptureDesktop desktop = new CaptureDesktop();
		desktop.StartCaptureDesktop("personTest", "dummy");
	}
	
	@Test(expected=Exception.class)
	public void testCaptureDesktopFailsWithOneNullParameter() {
		CaptureDesktop nullDesktop = new CaptureDesktop();

			nullDesktop.StartCaptureDesktop(null, "dummy");
			nullDesktop.StartCaptureDesktop("personTest", null);
	}
	
	
	@Test
	public void testStartCaptureDesktop() {
		
		CaptureDesktop desktop = new CaptureDesktop();
		desktop.StartCaptureDesktop("personTest", "dummy");
		// check file creation
		String videoName = "personTest" + "." + desktop.getFormat();
		 File videoFileObject = new File(videoName);
	       
	         
        //check videoCapture isStarted
        assertEquals(desktop.status(),true);
        
        //check  videoCapture isStopped
        desktop.StopCaptureDesktop();
        assertEquals(!desktop.status(),true);
        
     
        assertEquals(videoFileObject.exists(),true);
        videoFileObject.delete();

	}
	
}
