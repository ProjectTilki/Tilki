package kasirgalabs;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 *
 * @author goksu
 */
 
public class FaceDetectionController {

	@FXML
	private Button cameraButton;
	@FXML
	private ImageView originalFrame;
	private ScheduledExecutorService timer;
	private VideoCapture capture;
	private boolean cameraActive;
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;
	//ReportWriting rw=new ReportWriting();
	
	protected void init() {
		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;
		this.faceCascade.load("haarcascades/haarcascade_frontalface_alt.xml");
		originalFrame.setFitWidth(600);
		originalFrame.setPreserveRatio(true);
		startCamera();
	}

	protected void startCamera() {
		if (!this.cameraActive) {
			this.capture.open(0);
			if (this.capture.isOpened()) {
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
						for (int i = 0; i < 10; i++) {
							Mat frame1 = new Mat();
							capture.read(frame1);
							takeSS(i);
							takePicture(frame1, i);
						
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
				Thread t1 = new Thread(frameGrabber);
				Thread t2 = new Thread(takePhoto);

				t1.start();
				t2.start();
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
			
			} else {
				System.err.println("Failed to open the camera connection...");
			}

		} else {
			this.cameraActive = false;
			this.stopAcquisition();
		}
	}

	public void takePicture(Mat frame, int count) {
		BufferedImage img = matToBufferedImage(frame);
		try {
			File outputfile = new File("Photo" + count++ + ".png");
			ImageIO.write(img, "png", outputfile);
//			rw.addText("Taked Picture");
			//System.out.println("take picture");
		} catch (Exception e) {
			System.out.println("error");
		}
	
	}

	private Mat grabFrame() {
		Mat frame = new Mat();
		if (this.capture.isOpened()) {
			try {
				this.capture.read(frame);
				if (!frame.empty()) {
					// face detection
					this.detectAndDisplay(frame);
				}

			} catch (Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}

		return frame;
	}

	public void takeSS(int i) {
		String fileName="";
		try {
			Robot robot = new Robot();
			String format = "jpg";
			fileName = "FullScreenshot" + i + "." + format;
//			rw.addText("Taked SS");
			
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
			ImageIO.write(screenFullImage, format, new File(fileName));
			System.out.println("Screenshot taken!");
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	private void detectAndDisplay(Mat frame) {
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(grayFrame, grayFrame);
		if (this.absoluteFaceSize == 0) {
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0) {
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++)
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

	}

	private void stopAcquisition() {
		if (this.timer != null && !this.timer.isShutdown()) {
			try {
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened()) {
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
//		rw.submitText();
//		rw.addImage();
		this.stopAcquisition();
		Platform.exit();
		System.exit(0);

	}

	public static Image mat2Image(Mat frame) {
		try {
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		} catch (Exception e) {
			System.err.println("Cannot convert the Mat object: " + e);
			return null;
		}
	}

	public static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
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
