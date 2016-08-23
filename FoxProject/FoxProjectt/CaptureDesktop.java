


import com.teamdev.jxcapture.Codec;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;

import java.awt.*;
import java.io.File;

/**
 * This example demonstrates a primary desktop video capturing.
 * <pre>
 * Platforms:           All
 * Image Source:        Desktop
 * Output video format: WMV or MP4 depending on a platform 
 * Output file:         Desktop.wmv or Desktop.mp4 depending on a platform
 * 
 * @author Serge Piletsky
 */
public class CaptureDesktop {
   
	 VideoCapture videoCapture = VideoCapture.create();
      
        public void StartCaptureDesktop(String a,String b) throws Exception{

           
            videoCapture.setVideoSource(new Desktop());

            java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
            Codec videoCodec = videoCodecs.get(0);
            //System.out.println("videoCodec = " + videoCodec);	
            
            
            
            EncodingParameters encodingParameters = new EncodingParameters(new File(a+"_"+b+"." + videoCapture.getVideoFormat().getId()));
            // Resize output video
            encodingParameters.setSize(new Dimension(800, 600));
            encodingParameters.setBitrate(800000);
            encodingParameters.setFramerate(1);
            encodingParameters.setKeyFrameInterval(5000);
            encodingParameters.setCodec(videoCodec);
            // System.out.println("encodingParameters = " + encodingParameters);

            videoCapture.setEncodingParameters(encodingParameters);
            videoCapture.start();
        
            System.out.println("Recording started.");
      
       
        } 
        public void StopCaptureDesktop() throws Exception{
        	 videoCapture.stop();
             System.out.println("Done.");
        }
        
}
