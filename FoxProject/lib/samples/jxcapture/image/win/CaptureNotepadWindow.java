package jxcapture.image.win;

import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.InterpolationMode;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.WindowSource;
import com.teamdev.jxdesktop.WindowManager;
import com.teamdev.jxdesktop.ui.Window;

import java.awt.*;
import java.io.File;

/**
 * The example shows how to capture a Notepad window.
 * <pre>
 * Platforms:           Windows
 * Capture source:      WindowSource
 * Output image format: JPEG
 * Output file:         Notepad.jpg
 *
 * @author Ikryanov Vladimir
 * @author Serge Piletsky
 */
public class CaptureNotepadWindow {
    public static void main(String[] args) throws Exception {
        // Launching Notepad application
        Runtime.getRuntime().exec("notepad");

        // Finding Notepad window by title
        WindowManager windowManager = WindowManager.getInstance();
        Window notepad = (Window) windowManager.findWindow("Untitled - Notepad");

        if (notepad == null) {
            throw new RuntimeException("Notepad window was not found");
        }

        // Capturing notepad window
        ImageCapture imageCapture = ImageCapture.create(new WindowSource(notepad));
        imageCapture.setCaptureArea(new Rectangle(new Dimension(640, 480)));
//        imageCapture.setCaptureArea(new Rectangle(-20, -20, 640, 480));
        long before = System.currentTimeMillis();
        imageCapture.
                takeSnapshot()
                .resize(new Dimension(1024, 768), InterpolationMode.HighQualityBicubic)
                .save(new File("Notepad.jpg"), ImageFormat.JPEG, CompressionQuality.BEST);

        imageCapture.release();
        long after = System.currentTimeMillis();
        System.out.println("Operation took " + (after - before));
    }
}
