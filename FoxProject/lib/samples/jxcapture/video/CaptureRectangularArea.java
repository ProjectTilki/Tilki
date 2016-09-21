package jxcapture.video;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This example demonstrates how to capture a rectangular area of a screen.
 * <pre>
 * Platforms:           Windows
 * Image Source:        Desktop
 * Output video format: WMV or MP4
 * Output file:         Rectangle.wmv or Rectangle.mp4 depending on a platform
 *
 * @author Serge Piletsky
 */
public class CaptureRectangularArea {
    public static void main(String[] args) throws Exception {
        final VideoCapture videoCapture = VideoCapture.create();
        videoCapture.setCaptureArea(new Rectangle(0, 0, 800, 600));

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(0);
        System.out.println("videoCodec = " + videoCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("Rectangle." + videoCapture.getVideoFormat().getId()));
        encodingParameters.setSize(new Dimension(640, 480));
        encodingParameters.setBitrate(500000);
        encodingParameters.setFramerate(10);
        encodingParameters.setCodec(videoCodec);
        System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // shift the location of the capture area every second
                Rectangle area = videoCapture.getCaptureArea();
                Rectangle newArea = new Rectangle(new Point(area.x + 10, area.y + 10), area.getSize());
                videoCapture.setCaptureArea(newArea);
            }
        });
        timer.start();

        System.out.println("Recording started. Press 'Enter' to terminate.");
        System.in.read();
        timer.stop();
        videoCapture.stop();
        System.out.println("Done.");
    }
}