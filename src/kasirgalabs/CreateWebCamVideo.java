/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirgalabs;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.VideoSource;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author goksu
 */

public class CreateWebCamVideo implements Runnable{

    private VideoCapture videoCapture = VideoCapture.create();
    private final int KEYFRAMEINTERVAL = 1000;
        private String personName;
 CreateWebCamVideo(String a, String b) {
        a = a.toLowerCase();
        personName = a;
        
    }
    public void run() {
        List<VideoSource> availableVideoSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableVideoSources);

        if(availableVideoSources.isEmpty()) {
            throw new IllegalStateException(
                    "No external video sources available");
        }
        VideoSource webCamera = availableVideoSources.get(0);
        System.out.println("webCamera = " + webCamera);

        videoCapture.setVideoSource(webCamera);

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        System.out.println("videoCodecs = " + videoCodecs);
        if(videoCodecs.isEmpty()) {
            throw new IllegalStateException("No video codecs available");
        }

        Codec videoCodec = videoCodecs.get(2);
        System.out.println("videoCodec = " + videoCodec);
        CompressionQuality q = CompressionQuality.BEST;
        String videoName = "webcam"+personName + ".wmv";
        File videoFileObject = new File(personName + ".wmv");
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
        EncodingParameters encodingParameters = new EncodingParameters(videoFileObject);
        encodingParameters.setBitrate(500000);
        encodingParameters.setFramerate(1);
        encodingParameters.setKeyFrameInterval(1);
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setKeyFrameInterval(KEYFRAMEINTERVAL);
        encodingParameters.setCompressionQuality(q);
        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();
        //System.out.println("Recording started. Press 'Enter' to terminate.");
        //stop();
    }

    public void stop() {
            videoCapture.stop();
     
    }

    public boolean status() {
        return videoCapture.isStarted();
    }
}
