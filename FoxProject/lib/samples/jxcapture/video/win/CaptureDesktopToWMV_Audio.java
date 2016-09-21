package jxcapture.video.win;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.InterpolationMode;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.audio.AudioCodec;
import com.teamdev.jxcapture.audio.AudioEncodingParameters;
import com.teamdev.jxcapture.audio.AudioSource;
import com.teamdev.jxcapture.video.VideoFormat;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * This example demonstrates a primary desktop video capturing.
 * <pre>
 * Platforms:           Windows
 * Image source:        Desktop
 * Output video format: WMV
 *
 * Output file:         Desktop.wmv
 *
 * @author Serge Piletsky
 */
public class CaptureDesktopToWMV_Audio {

    static boolean useAudio = true;
    static boolean usePause = true;

    public static void main(String[] args) throws Exception {
        EncodingParameters encodingParameters = new EncodingParameters(new File("Desktop.wmv"));
        encodingParameters.setBitrate(1500000);
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setInterpolationMode(InterpolationMode.HighQuality);

        VideoCapture videoCapture = VideoCapture.create(VideoFormat.WMV);

        List<Codec> codecs = videoCapture.getVideoCodecs();
        System.out.println("Available video codecs:");
        for (Codec
                codec : codecs) {
            System.out.println("Video codec: " + codec);
        }
        Codec preferredCodec = codecs.get(1);
        System.out.println("Selected video codec = " + preferredCodec);

        encodingParameters.setCodec(preferredCodec);

        if (useAudio) {
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
                videoCapture.setAudioSource(audioSource);

                List<AudioCodec> audioCodecs = videoCapture.getAudioCodecs();
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
        }
        System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.start(encodingParameters);
        if (usePause) {
            System.out.println("Recording started. Press 'Enter' to pause.");
            System.in.read();
            videoCapture.pause();
            System.out.println("Recording started. Press 'Enter' to resume.");
            System.in.read();
            videoCapture.start();
        }
        System.out.println("Recording started. Press 'Enter' to stop.");
        System.in.read();
        videoCapture.stop();
        System.out.println("Done.");
    }
}
