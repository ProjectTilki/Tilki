package kasirgalabs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 *
 * @author goksu
 */
public class FaceDetectionController {

    @FXML
    private ImageView originalFrame;
    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive;
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    ReportWriting rw;
    int count = 0;
    TrustScore faceDetectScore = new TrustScore();

static ArrayList<BufferedImage> imgarry = new ArrayList<BufferedImage>();
    protected void init(ReportWriting rw) {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        this.faceCascade.load("haarcascades/haarcascade_frontalface_alt.xml");
        originalFrame.setFitWidth(600);
        originalFrame.setPreserveRatio(true);
        this.rw = rw;
        startCamera();
    }

    protected void startCamera() {
        if(!this.cameraActive) {
            this.capture.open(0);
            if(this.capture.isOpened()) {
                this.cameraActive = true;
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        Mat frame = grabFrame();
                        // convert and show the frame

                        Image imageToShow = mat2Image(frame);
                        updateImageView(originalFrame, imageToShow);
                    }
                };
                Runnable takePhoto = new Runnable() {
                    @Override
                    public void run() {

                        Mat frame1 = new Mat();
                        capture.read(frame1);
                        takePicture(frame1);
                        try {
                            Thread.sleep(600000);// 10 dk
                        }
                        catch(InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                };
                Thread t1 = new Thread(frameGrabber);
                Thread t2 = new Thread(takePhoto);

                t1.start();
                t2.start();
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33,
                        TimeUnit.MILLISECONDS);

            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Failed to open the camera connection... ", "alert",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("Failed to open the camera connection...");
            }

        }
        else {
            this.cameraActive = false;
            System.out.println(
                    "girdiii");
            rw.submitText();

            this.stopAcquisition();
        }
    }

    public void takePicture(Mat frame) {
        BufferedImage img = matToBufferedImage(frame);
        imgarry.add(img);
        System.out.println("kasirgalabs.FaceDetectionController.takePicture()"+img); 
        //rw.addText("Taked Picture",0);
        
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(
//                    "yyyyMMddHHmmss");
//            Date date = new Date();
//            String s = dateFormat.format(date) + ".png";
//            File outputfile = new File(s);
//
//            ImageIO.write(img, "png", outputfile);
//            rw.addText("Taked Picture");

            //System.out.println("take picture");
//        }
//        catch(Exception e) {
//            System.out.println("error");
//        }

    }

    private Mat grabFrame() {
        Mat frame = new Mat();
        if(this.capture.isOpened()) {
            try {
                this.capture.read(frame);
                if(!frame.empty()) {
                    // face detection
                    this.detectAndDisplay(frame);
                }

            }
            catch(Exception e) {
                System.err.println(
                        "Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        if(this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if(Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2,
                0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize),
                new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        if(facesArray.length == 0) {
            count++;

            if(count % 50 == 0) {
                // System.out.println("yüz bulunamadı");
                
                faceDetectScore.skorArttir(2);
                rw.addText("Couldn't find face ",0);
                takePicture(frame);
            }
        }
        else {
            count = 0;
        }
        for(int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(),
                    new Scalar(0, 255, 0), 3);
        }
        System.out.println("Face Detection Score : " + faceDetectScore.getSkor());

    }

    private void stopAcquisition() {
        if(this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch(InterruptedException e) {
                // log any exception
                System.err.println(
                        "Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if(this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    private void updateImageView(ImageView view, Image image) {
        onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    protected void setClosed() {
        CreateVideo.createVideo(imgarry);
        this.stopAcquisition();
    }

    public static Image mat2Image(Mat frame) {
        try {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch(Exception e) {
            System.err.println("Cannot convert the Mat object: " + e);
            return null;
        }
    }

    public static <T> void onFXThread(final ObjectProperty<T> property,
            final T value) {
        Platform.runLater(() -> {
            property.set(value);
        });
    }

    private static BufferedImage matToBufferedImage(Mat original) {
        // init
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
}
