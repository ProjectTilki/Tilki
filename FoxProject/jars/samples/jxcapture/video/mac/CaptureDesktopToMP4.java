package jxcapture.video.mac;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import com.teamdev.jxcapture.video.Framerate;
import com.teamdev.jxcapture.video.VideoFormat;

import java.io.File;
import java.util.List;

/**
 * This example demonstrates the full screen video capture.
 * <pre>
 * Platform:            Mac OS X
 * Image Source:        Desktop
 * Output video format: MP4
 * Output file:         Desktop.mp4
 *
 * @author Ikryanov Vladimir
 */
public class CaptureDesktopToMP4 {
    public static void main(String[] args) throws Exception {

        // Create MacVideoCapture instance
        VideoCapture videoCapture = VideoCapture.create(VideoFormat.MP4);
        videoCapture.setVideoSource(new Desktop());

        List<Codec> codecs = videoCapture.getVideoCodecs();
        System.out.println("Supported video codecs: " + codecs);
        Codec videoCodec = codecs.get(0);

        // Configure video videoEncoding settings
        EncodingParameters encodingParameters = new EncodingParameters(new File("Desktop.mp4"));
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setFramerate(Framerate.MAX);
        encodingParameters.setCompressionQuality(CompressionQuality.BEST);

        // Start video recording
        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();

        System.out.println("Recording started. Press 'Enter' to terminate.");
        System.in.read();

        // Stop video recording
        videoCapture.stop();
        System.out.println("Done.");
    }
}
