package jxcapture.video.win;

import com.teamdev.jxcapture.BitrateMode;
import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.audio.AudioChannels;
import com.teamdev.jxcapture.audio.AudioCodec;
import com.teamdev.jxcapture.audio.AudioEncodingParameters;
import com.teamdev.jxcapture.audio.AudioSource;
import com.teamdev.jxcapture.video.VideoFormat;
import com.teamdev.jxcapture.video.WindowSource;
import com.teamdev.jxdesktop.WindowManager;

import java.io.File;
import java.util.List;

/**
 * This example demonstrates how to create video capture from a browser window.
 * <pre>
 * Platforms:           Windows
 * Image source:        Window
 * Output video format: WMV
 * Output file:         Window.wmv
 *
 * @author Serge Piletsky
 */
public class CaptureExplorerWindow {
    public static void main(String[] args) throws Exception {
        // Launch an explorer and navigate to URL
        Runtime.getRuntime().exec("explorer http://www.teamdev.com/jxcapture");
        Thread.sleep(3000);

        // Find explorer window by the title
        WindowManager windowManager = WindowManager.getInstance();
        com.teamdev.jxdesktop.ui.Window explorerWindow = (com.teamdev.jxdesktop.ui.Window) windowManager.findWindow("JxCapture  TeamDev Ltd. - Windows Internet Explorer");
        if (explorerWindow == null) {
            explorerWindow = (com.teamdev.jxdesktop.ui.Window) windowManager.findWindow("JxCapture — TeamDev Ltd. - Mozilla Firefox");
        }

        if (explorerWindow == null) {
            throw new IllegalStateException("Could not find a required window to capture video");
        }

        VideoCapture videoCapture = VideoCapture.create(VideoFormat.WMV);

        videoCapture.setVideoSource(new WindowSource(explorerWindow));
//        videoCapture.setCaptureArea(new Rectangle(10, 10, 800, 600));

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(2);
        System.out.println("videoCodec = " + videoCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("Window.wmv"));
        encodingParameters.setBitrate(2500000);
        encodingParameters.setFramerate(10);
        encodingParameters.setKeyFrameInterval(1);
        encodingParameters.setCodec(videoCodec);

        List<AudioSource> audioSources = AudioSource.getAvailable();
        System.out.println("audioSources = " + audioSources);
        if (audioSources == null || audioSources.size() == 0) {
            throw new IllegalStateException("No audio sources found");
        }

        AudioSource audioSource = audioSources.get(0);
        System.out.println("audioSource = " + audioSource);
        videoCapture.setAudioSource(audioSource);

        List<AudioCodec> audioCodecs = videoCapture.getAudioCodecs();
        System.out.println("audioCodecs = " + audioCodecs);
        if (audioCodecs.size() == 0) {
            throw new IllegalStateException("No audio codecs available for this video capture");
        }

        AudioEncodingParameters audioEncodingParameters = new AudioEncodingParameters();
        audioEncodingParameters.setCodec(audioCodecs.get(1));
        audioEncodingParameters.setAudioChannels(AudioChannels.Stereo);
        audioEncodingParameters.setBitrate(50000);
        audioEncodingParameters.setBitrateMode(BitrateMode.CBR);
        encodingParameters.setAudioEncoding(audioEncodingParameters);

        System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();

        System.out.println("Recording started. Press 'Enter' to terminate.");
        System.in.read();
        videoCapture.stop();
        System.out.println("Done.");
    }
}