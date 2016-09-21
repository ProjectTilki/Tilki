/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package jxcapture.video.win;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.audio.AudioCodec;
import com.teamdev.jxcapture.audio.AudioEncodingParameters;
import com.teamdev.jxcapture.audio.AudioSource;
import com.teamdev.jxcapture.video.VideoFormat;
import com.teamdev.jxcapture.video.VideoSource;

import java.io.File;
import java.util.List;

/**
 * This example demonstrates video capturing from a web camera with audio.
 * <pre>
 * Platforms:           Windows
 * Image source:        WebCamera
 * Output video format: WMV
 * Output file:         WebCamera.wmv
 *
 * @author Serge Piletsky
 */
public class CaptureVideoFromWebCameraWithAudio {

    public static void main(String[] args) throws Exception {
        VideoCapture videoCapture = VideoCapture.create(VideoFormat.WMV);

        List<VideoSource> availableVideoSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableVideoSources);

        if (availableVideoSources.isEmpty()) {
            throw new IllegalStateException("No external video sources available");
        }

        VideoSource webCamera = availableVideoSources.get(0);
        System.out.println("webCamera = " + webCamera);

        videoCapture.setVideoSource(webCamera);

        List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(2);
        System.out.println("videoCodec = " + videoCodec);

        EncodingParameters encodingParameters = new EncodingParameters(new File("WebCamera.wmv"));
        encodingParameters.setBitrate(500000);
        encodingParameters.setFramerate(10);
        encodingParameters.setKeyFrameInterval(1);
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
