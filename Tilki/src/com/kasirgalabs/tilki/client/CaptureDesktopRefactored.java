package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.FileManager;
import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

public class CaptureDesktopRefactored {

    private static volatile CaptureDesktopRefactored instance = null;
    private final FileManager fileManager = FileManager.getInstance();
    private VideoCapture videoCapture;

    public static CaptureDesktopRefactored getInstance() {
        if(instance == null) {
            synchronized(CaptureDesktopRefactored.class) {
                if(instance == null) {
                    instance = new CaptureDesktopRefactored();
                }
            }
        }
        return instance;
    }

    public CaptureDesktopRefactored() {
        try {
            this.videoCapture = VideoCapture.create();
        }
        catch(Exception e) {
            ClientExceptionHandler.logAnException(e);
        }
    }

    public void StartCaptureDesktop(String id) {
        if(!isAvailable()) {
            return;
        }
        setVideoSource();
        File videoFile = getVideoFile(id);
        Codec videoCodec = getVideoCodec();

        EncodingParameters encodingParameters = new EncodingParameters(videoFile);

        resizeOutputVideo(encodingParameters, videoCodec);
        setCompressionQuality(encodingParameters);

        videoCapture.setEncodingParameters(encodingParameters);
        try {
            videoCapture.start();
            fileManager.trackFile(videoFile);
        }
        catch(Exception e) {
            ClientExceptionHandler.logAnException(e);
            videoCapture = null;
        }
    }

    public void StopCaptureDesktop() {
        if(videoCapture != null && videoCapture.isStarted()) {
            videoCapture.stop();
        }
    }

    private boolean isAvailable() {
        return !(videoCapture == null && videoCapture.isStarted());
    }

    private void setVideoSource() {
        videoCapture.setVideoSource(new Desktop());
    }

    private File getVideoFile(String id) {
        String format = videoCapture.getVideoFormat().getId().toString();
        return fileManager.getValidFile(id + "." + format);
    }

    private Codec getVideoCodec() {
        List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        return videoCodecs.get(0);
    }

    private void resizeOutputVideo(EncodingParameters encodingParameters,
            Codec videoCodec) {
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(1);
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setKeyFrameInterval(1);
    }

    private void setCompressionQuality(EncodingParameters encodingParameters) {
        encodingParameters.setCompressionQuality(CompressionQuality.BEST);
    }
}
