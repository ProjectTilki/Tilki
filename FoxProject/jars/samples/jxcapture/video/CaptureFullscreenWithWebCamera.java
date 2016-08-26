package jxcapture.video;


import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.audio.AudioCodec;
import com.teamdev.jxcapture.audio.AudioEncodingParameters;
import com.teamdev.jxcapture.audio.AudioSource;
import com.teamdev.jxcapture.video.FullScreen;
import com.teamdev.jxcapture.video.VideoSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * This example demonstrates how to capture a full screen video along with video from a web-camera.
 *
 * @author Serge Piletsky
 */
public class CaptureFullscreenWithWebCamera {

    public static void main(String[] args) throws Exception {
        // Create a full screen video capture
        VideoCapture fullScreenVideoCapture = VideoCapture.create();
        fullScreenVideoCapture.setVideoSource(new FullScreen());

        List<VideoSource> availableVideoSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableVideoSources);

        if (availableVideoSources.isEmpty()) {
            throw new IllegalStateException("No external video sources available");
        }

        VideoSource webCamera = availableVideoSources.get(0);
        System.out.println("webCamera = " + webCamera);

        // Crate a web camera image capture
        ImageCapture webCameraImageCapture = ImageCapture.create();
        webCameraImageCapture.setImageSource(webCamera);


        // Configure output parameters
        List<Codec> videoCodecs = fullScreenVideoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(2);
        System.out.println("videoCodec = " + videoCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("FullScreenWithWebCamera.wmv"));
        encodingParameters.setBitrate(1000000);
        encodingParameters.setFramerate(10);
        encodingParameters.setCodec(videoCodec);

        System.out.println("Available audio recording sources:");
        List<AudioSource> audioSources = AudioSource.getAvailable();
        for (AudioSource audioSource : audioSources) {
            System.out.println("audioSource = " + audioSource);
        }
        if (audioSources.isEmpty()) {
            System.err.println("No audio sources available");
        } else {
            AudioSource audioSource = audioSources.get(0);
            System.out.println("Selected audio source = " + audioSource);
            fullScreenVideoCapture.setAudioSource(audioSource);

            List<AudioCodec> audioCodecs = fullScreenVideoCapture.getAudioCodecs();
            if (audioSources.isEmpty()) {
                System.err.println("No audio codecs available");
            } else {
                System.out.println("Available audio codecs:");
                for (AudioCodec audioCodec : audioCodecs) {
                    System.out.println("audioCodec = " + audioCodec);
                }

                // Enable and configure audio encoding
                AudioEncodingParameters audioEncoding = new AudioEncodingParameters();

                AudioCodec audioCodec = audioCodecs.get(0);
                System.out.println("Selected audio codec = " + audioCodec);
                audioEncoding.setCodec(audioCodec);

                encodingParameters.setAudioEncoding(audioEncoding);
            }
        }
        System.out.println("encodingParameters = " + encodingParameters);

        // Show web-camera view on the desktop
        WebCameraView webCameraView = new WebCameraView(webCameraImageCapture);
        webCameraView.show();

        // Start recording video
        fullScreenVideoCapture.start(encodingParameters);
        System.out.println("Recording started. Press 'Enter' to stop.");
        System.in.read();
        fullScreenVideoCapture.stop();
        webCameraView.hide();
        System.out.println("Done.");
    }

    private static class WebCameraView {
        private static final Dimension VIEW_DIMENSIONS = new Dimension(320, 240);
        private static final Point VIEW_LOCATION = new Point(100, 100);
        private static final int UPDATE_INTERVAL = 100;

        private JWindow webCameraView;
        private BufferedImage snapshot;
        private Timer updateTimer;

        public WebCameraView(final ImageCapture webCameraCapture) {
            webCameraView = new JWindow() {
                @Override
                public void paint(Graphics g) {
                    if (snapshot != null) {
                        g.drawImage(snapshot, 0, 0, VIEW_DIMENSIONS.width, VIEW_DIMENSIONS.height, null);
                    }
                }
            };
            updateTimer = new Timer(UPDATE_INTERVAL, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    webCameraCapture.takeSnapshot();
                    if (snapshot != null) {
                        snapshot.flush();
                    }
                    snapshot = webCameraCapture.getImage();
                    webCameraView.repaint();
                }
            });
        }

        public void show() {
            webCameraView.setLocation(VIEW_LOCATION);
            webCameraView.setSize(VIEW_DIMENSIONS);
            webCameraView.setAlwaysOnTop(true);
            webCameraView.setVisible(true);
            updateTimer.start();
        }

        public void hide() {
            updateTimer.stop();
            webCameraView.dispose();
        }
    }
}
