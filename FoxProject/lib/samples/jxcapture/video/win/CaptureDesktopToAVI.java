package jxcapture.video.win;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.InterpolationMode;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import com.teamdev.jxcapture.video.win.AVICapture;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * This example demonstrates the a primary desktop video capturing.
 * <pre>
 * Platforms:           Windows
 * Image source:        Desktop
 * Output video format: AVI
 * Output file:         Desktop.avi
 *
 * @author Serge Piletsky
 */
public class CaptureDesktopToAVI {

    public static void main(String[] args) throws Exception {

        VideoCapture videoCapture = new AVICapture();
        videoCapture.setVideoSource(new Desktop());

        List<Codec> codecs = videoCapture.getVideoCodecs();
        if (codecs.isEmpty()) {
            throw new IllegalStateException("There are no suitable codecs available");
        }

        System.out.println("Available video codecs:");
        for (Codec codec : codecs) {
            System.out.println(codec);
        }
        Codec preferredCodec = codecs.get(0);
        System.out.println("preferredCodec = " + preferredCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("Desktop.avi"));
        encodingParameters.setBitrate(1000000);
        encodingParameters.setSize(new Dimension(640, 480));
        encodingParameters.setInterpolationMode(InterpolationMode.HighQuality);
        encodingParameters.setCodec(preferredCodec);
        System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.start(encodingParameters);
        System.out.println("Recording started. Press 'Enter' to pause.");
        System.in.read();
        videoCapture.pause();
        System.out.println("Recording started. Press 'Enter' to resume.");
        System.in.read();
        videoCapture.start();
        System.out.println("Recording started. Press 'Enter' to stop.");
        System.in.read();
        videoCapture.stop();
        System.out.println("Done.");
    }
}
