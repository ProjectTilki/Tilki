package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.FileManager;
import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import java.awt.Dimension;
import java.io.File;

public class CaptureDesktop {

    private String personName;
    private String format;
    private VideoCapture videoCapture = VideoCapture.create();

    public void StartCaptureDesktop(String a, String b) {
        videoCapture.setVideoSource(new Desktop());
        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(0);

        a = a.toLowerCase();
        personName = a;
        format = videoCapture.getVideoFormat().getId().toString();
        //String videoName = FileManager.generateFileName(
        //        personName + "." + format);
        FileManager fileManager = FileManager.getInstance();
        File video = fileManager.getValidFile(personName + "." + format);

        CompressionQuality q = CompressionQuality.BEST;

        EncodingParameters encodingParameters = new EncodingParameters(video);
        // Resize output video
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(1);
        encodingParameters.setCodec(videoCodec);

        encodingParameters.setKeyFrameInterval(1);
        encodingParameters.setCompressionQuality(q);

        videoCapture.setEncodingParameters(encodingParameters);
        try {
            videoCapture.start();
        }
        catch(Exception e) {
            ClientExceptionHandler.logAnException(e);
        }
    }

    public String getPersonName() {
        return personName;
    }

    public String getFormat() {
        return format;
    }

    public void StopCaptureDesktop() {
        videoCapture.stop();
    }

    public boolean status() {
        return videoCapture.isStarted();
    }
}
