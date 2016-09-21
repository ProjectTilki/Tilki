package jxcapture.image;

import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.FullScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The example demonstrates full screen capturing.
 * <pre>
 * Platforms:           All
 * Image source:        FullScreen
 * Output image format: JPEG
 * Output file:         FullScreen.jpg
 *
 * @author Serge Piletsky
 * @author Ikryanov Vladimir
 */
public class CaptureFullScreen {
    public static void main(String[] args) throws Exception {
        // create image capture for a full screen
        FullScreen imageSource = new FullScreen();
        ImageCapture imageCapture = ImageCapture.create(imageSource);

        imageCapture.takeSnapshot();

        // save captured image to a file in specified format
        imageCapture.save(new File("FullScreen.jpg"), ImageFormat.JPEG, CompressionQuality.HIGH);

        // convert image capture to Java BufferedImage
        BufferedImage snapshot = imageCapture.getImage();

        // release image capture resources
        imageCapture.release();

        // display snapshot in a window
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(new JLabel(new ImageIcon(snapshot))), BorderLayout.CENTER);
        frame.setSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
