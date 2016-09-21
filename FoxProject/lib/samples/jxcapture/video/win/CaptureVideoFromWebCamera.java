package jxcapture.video.win;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.Configurable;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.VideoFormat;
import com.teamdev.jxcapture.video.VideoSource;

import java.io.File;
import java.util.List;

/**
 * This example demonstrates the video capture from web camera.
 * <pre>
 * Platforms:           Windows
 * Image source:        WebCamera
 * Output video format: WMV
 * Output file:         WebCamera.wmv
 *
 * @author Serge Piletsky
 */
public class CaptureVideoFromWebCamera {

    public static void main(String[] args) throws Exception {
        VideoCapture videoCapture = VideoCapture.create(VideoFormat.WMV);

        List<VideoSource> availableVideoSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableVideoSources);

        if (availableVideoSources.isEmpty()) {
            throw new IllegalStateException("No external video sources available");
        }
        VideoSource webCamera = availableVideoSources.get(0);
        if (webCamera instanceof Configurable) {
            ((Configurable) webCamera).configure();
        }
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
        encodingParameters.setBitrate(500000);
        encodingParameters.setFramerate(10);
        encodingParameters.setKeyFrameInterval(1);
        encodingParameters.setCodec(videoCodec);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();
        System.out.println("Recording started. Press 'Enter' to terminate.");
        System.in.read();
        videoCapture.stop();
    }
}
