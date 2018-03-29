package kasirgalabs.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import kasirgalabs.CaptureDesktop;

public class TestCaptureDesktop {
	
	private CaptureDesktop desktop;
	private CaptureDesktop nullDesktop;
	
	
	@Before
	public void initialize () {
		desktop = new CaptureDesktop();
		desktop.StartCaptureDesktop("personTest", "dummy");
	}
	
	@Test
	public void testCaptureDesktopFailsWithOneNullParameter() {
		nullDesktop = new CaptureDesktop();
		
		try {
			nullDesktop.StartCaptureDesktop(null, "dummy");
			nullDesktop.StartCaptureDesktop("personTest", null);
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStartCaptureDesktop() {
		// check file creation
		String videoName = "personTest" + "." + desktop.getFormat();
		 File videoFileObject = new File(videoName);
	        if(videoFileObject.exists()) // File exists, try finding new file name.
	        {
	            for(int i = 0; i < 100; i++) {
	                videoFileObject = new File(i + "_" + videoName);
	                if(!videoFileObject.exists()) {
	                    videoName = i + "_" + videoName;
	                    break;
	                }
	            }
	        }
        if(videoFileObject.exists()) // File exists
        {
        	assert(videoFileObject.exists());
        	videoFileObject.delete();
        }

	}
	
	@Test
	public void testVideoCaptureIsStarted() {
        //check videoCapture isStarted
        assertTrue(desktop.status());
	}
	
	@Test
	public void testVideoCaptureisStopped() {
        //check  videoCapture isStopped
        desktop.StopCaptureDesktop();
        assertTrue(!desktop.status());
	}
	
	
}
