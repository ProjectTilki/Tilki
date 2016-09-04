import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import com.teamdev.jxcapture.video.FullScreen;
import com.teamdev.jxcapture.video.VideoFormat;
import java.awt.*;
import java.io.File;
import java.util.Calendar;

public class CaptureDesktop {
    
   public String personName;
   public String format;
    VideoCapture videoCapture = VideoCapture.create();

    public void StartCaptureDesktop(String a, String b){
        videoCapture.setVideoSource(new FullScreen());
        java.util.List<VideoFormat> videoFormats = VideoCapture.getAvailableFormats();
        for(VideoFormat c:videoFormats)
            System.out.println("a "+c);
                
        
        java.util.List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        for(Codec c:videoCodecs)
            System.out.println("b "+c);
        
        Codec videoCodec = videoCodecs.get(0);
        a = a.toLowerCase();
        b = b.toLowerCase();
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        personName =a + "_" + b;
        format = videoCapture.getVideoFormat().getId().toString();        
        File video = new File(personName + "_" + hour + " " + ((minute < 10) ? ("0"+minute) : minute) +"." + format);        
        EncodingParameters encodingParameters = new EncodingParameters(video);
        // Resize output video
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(1);
        encodingParameters.setKeyFrameInterval(5000);
        encodingParameters.setCodec(videoCodec);
        // System.out.println("encodingParameters = " + encodingParameters);

        videoCapture.setEncodingParameters(encodingParameters);
        videoCapture.start();
        System.out.println("Recording started.");

    }

    public void StopCaptureDesktop(){
        videoCapture.stop();
        System.out.println("Done.");
    }
    public boolean status(){
        return videoCapture.isStarted();
    }
}