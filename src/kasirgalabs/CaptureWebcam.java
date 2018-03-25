package kasirgalabs;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.VideoFormat;
import com.teamdev.jxcapture.video.VideoSource;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

public class CaptureWebcam implements Runnable{
    private VideoCapture videoCapture = VideoCapture.create(VideoFormat.WMV);

    public void stopCaptureWebcam() {
        videoCapture.stop();  
    }
    
    public boolean status() {
        return videoCapture.isStarted();
    }

    @Override
    public void run() {
        List<VideoSource> availableVideoSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableVideoSources);

        if (availableVideoSources.isEmpty()) {
            throw new IllegalStateException("No external video sources available");
        }
        VideoSource webCamera = availableVideoSources.get(0);
        System.out.println("webCamera = " + webCamera);

        videoCapture.setVideoSource(webCamera);

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        System.out.println("videoCodecs = " + videoCodecs);
        if (videoCodecs.isEmpty()) {
            throw new IllegalStateException("No video codecs available");
        }

        Codec videoCodec = videoCodecs.get(2);
        System.out.println("videoCodec = " + videoCodec);
        
        EncodingParameters encodingParameters = new EncodingParameters(new File("WebCamera.wmv"));
        encodingParameters.setSize(new Dimension(640, 480));
        encodingParameters.setBitrate(500000);
        encodingParameters.setFramerate(10);
        encodingParameters.setKeyFrameInterval(1);
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setCompressionQuality(CompressionQuality.BEST);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();
             
    }
}
