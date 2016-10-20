package kasirgalabs;

import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.FullScreen;
import java.awt.Dimension;
import java.io.File;

public class CaptureDesktop {

    private String personName;
    private String format;
    private VideoCapture videoCapture = VideoCapture.create();

    public void StartCaptureDesktop(String a, String b) {
        videoCapture.setVideoSource(new FullScreen());

        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();

        Codec videoCodec = videoCodecs.get(0);
        a = a.toLowerCase();
        personName = a;
        format = videoCapture.getVideoFormat().getId().toString();
        String videoName = personName + "." + format;
        File videoFileObject = new File(personName + "." + format);
        if(videoFileObject.exists()) // File exists, try finding new file name.
            for(int i = 0; i < 100; i++) {
                videoFileObject = new File(i + "_" + videoName);
                if(!videoFileObject.exists()) {
                    videoName = i + "_" + videoName;
                    break;
                }
            }
        CompressionQuality q = CompressionQuality.BEST;

        File video = new File(videoName);
        EncodingParameters encodingParameters = new EncodingParameters(video);
        // Resize output video
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(1);
        encodingParameters.setKeyFrameInterval(1000);
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setCompressionQuality(q);

        //     System.out.println("encodingParameters = " + encodingParameters);
        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();
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
