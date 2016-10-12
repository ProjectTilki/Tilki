package jxcapture.image;

import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.Desktop;

import java.awt.*;
import java.io.File;

/**
 * The sample demonstrates capturing of all desktops.
 * <pre>
 * Platforms:           All
 * Image source:        Desktop
 * Output image format: PNG
 * Output file:         Desktop.png
 *
 * @author Sergei Piletsky
 * @author Ikryanov Vladimir
 */
public class CaptureDesktops {
    public static void main(String[] args) throws Exception {
        ImageCapture imageCapture = ImageCapture.create();

        int numberOfDisplays = Desktop.getNumberOfDisplays();
        for (int i = 0; i < numberOfDisplays; i++) {
            imageCapture.setImageSource(new Desktop(i));

            long before = System.currentTimeMillis();
            Dimension size = imageCapture.getImageSource().getSize();
            imageCapture.
                    takeSnapshot()
                    .save(new File("Desktop[" + i + "], size[Width=" + size.width + ",Height=" + size.height + "].png"), ImageFormat.PNG);

            long after = System.currentTimeMillis();
            System.out.println("Operation took " + (after - before) + " milliseconds.");
        }

        imageCapture.release();
    }
}