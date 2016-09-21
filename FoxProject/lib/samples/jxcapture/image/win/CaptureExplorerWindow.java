package jxcapture.image.win;

import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.WindowSource;
import com.teamdev.jxdesktop.WindowManager;

import java.awt.*;
import java.io.File;

/**
 * The example shows how to capture a browser window.
 * <pre>
 * Platforms:           Windows
 * Capture source:      WindowSource
 * Output image format: PNG
 * Output file:         Explorer.png
 *
 * @author Serge Piletsky
 */
public class CaptureExplorerWindow {
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec("explorer http://www.teamdev.com/jxcapture");
        Thread.sleep(3000);

        // Find explorer window by the title
        WindowManager windowManager = WindowManager.getInstance();
        com.teamdev.jxdesktop.ui.Window explorerWindow = (com.teamdev.jxdesktop.ui.Window) windowManager.findWindow("JxCapture — TeamDev Ltd. - Windows Internet Explorer");
        if (explorerWindow == null) {
            explorerWindow = (com.teamdev.jxdesktop.ui.Window) windowManager.findWindow("JxCapture — TeamDev Ltd. - Mozilla Firefox");
        }

        if (explorerWindow == null) {
            throw new IllegalStateException("Could not find a required window to capture image");
        }

        // Capturing notepad window
        ImageCapture imageCapture = ImageCapture.create(new WindowSource(explorerWindow));
        imageCapture.setCaptureArea(new Rectangle(50, 50, 800, 600));
        long before = System.currentTimeMillis();
        imageCapture.
                takeSnapshot()
                .resize(new Dimension(640, 480))
                .save(new File("Explorer.png"), ImageFormat.PNG);

        imageCapture.release();
        long after = System.currentTimeMillis();
        System.out.println("Operation took " + (after - before));
    }
}