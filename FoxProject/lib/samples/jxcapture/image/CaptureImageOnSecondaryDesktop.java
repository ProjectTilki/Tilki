/*
 * Copyright (c) 2000-2012 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package jxcapture.image;

import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.image.ImageFormat;
import com.teamdev.jxcapture.video.Desktop;

import java.awt.*;
import java.io.File;

/**
 * The sample demonstrates capturing of a secondary display.
 * <pre>
 * Platforms:           All
 * Image source:        Desktop
 * Output image format: PNG
 * Output file:         Desktop.png
 *
 * @author Sergei Piletsky
 */
public class CaptureImageOnSecondaryDesktop {
    public static void main(String[] args) throws Exception {
        ImageCapture imageCapture = ImageCapture.create();

        int numberOfDisplays = Desktop.getNumberOfDisplays();

        if (numberOfDisplays > 1) {
            Desktop source = new Desktop(1);
            imageCapture.setImageSource(source);
            imageCapture.setCaptureArea(new Rectangle(100, 100, 800, 600));
            Rectangle captureArea = imageCapture.getCaptureArea();
            System.out.println("captureArea = " + captureArea);
            imageCapture.save(new File("Desktop[" + source.getNumber() + "], size[Width=" + captureArea.width + ",Height=" + captureArea.height + "].png"), ImageFormat.PNG);
        }
        imageCapture.release();
    }
}