package jxcapture.video;

import com.teamdev.jxcapture.Codec;
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
    public static void main(String[] args) throws Exception {
        VideoCapture videoCapture = VideoCapture.create();
        videoCapture.setVideoSource(new Desktop());

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(0);
        System.out.println("videoCodec = " + videoCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("Desktop." + videoCapture.getVideoFormat().getId()));
        // Resize output video
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(10);
        encodingParameters.setCodec(videoCodec);
        System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();

        System.out.println("Recording started. Press 'Enter' to terminate.");
        System.in.read();
        videoCapture.stop();
        System.out.println("Done.");
    }
}
