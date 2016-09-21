package jxcapture.image.win;

import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.VideoSource;

import java.awt.*;
import java.io.File;

/**
 * The sample demonstrates capturing of snapshots from a web camera.
 * <pre>
 * Platforms:           Windows
 * Capture source:      WebCamera
 * Output image format: PNG
 * Output file:         WebCamera-{rectangle}.png
 *
 * @author Sergei Piletsky
 */
public class CaptureImageFromWebCamera {
    // Supposing that video source image size is 640x480
    private static Rectangle[] CAPTURE_RECTANGLES = {
            new Rectangle(0, 0, 800, 600),
            new Rectangle(100, 0, 800, 600),
            new Rectangle(-100, 0, 800, 600),
            new Rectangle(0, 100, 800, 600),
            new Rectangle(100, 100, 800, 600),
            new Rectangle(0, -100, 800, 600),
            new Rectangle(-100, -100, 800, 600),
    };

    public static void main(String[] args) throws Exception {
        ImageCapture imageCapture = ImageCapture.create();

        java.util.List<VideoSource> availableSources = VideoSource.getAvailable();
        System.out.println("availableVideoSources = " + availableSources);

        if (availableSources.isEmpty()) {
            throw new IllegalStateException("No external video sources are available");
        }

        VideoSource webCamera = availableSources.get(0);
        System.out.println("webCamera = " + webCamera);
        imageCapture.setImageSource(webCamera);

        for (Rectangle rectangle : CAPTURE_RECTANGLES) {
            imageCapture.setCaptureArea(rectangle);
            imageCapture.takeSnapshot();
            imageCapture.save(new File("WebCamera-" + rectangle + ".png"), ImageFormat.PNG);
            imageCapture.release();
        }
    }
}