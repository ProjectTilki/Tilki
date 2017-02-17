package com.kasirgalabs.tilki.client;

/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import com.kasirgalabs.tilki.client.FileManager;
import com.kasirgalabs.tilki.client.User;
import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.CompressionQuality;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.video.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

public final class CaptureDesktop {
    public static CaptureDesktop instance;
    private FileManager fileManager;
    private VideoCapture videoCapture;
    private File videoFile;

    private CaptureDesktop() {
        try {
            fileManager = FileManager.getInstance();
            init();
        } catch(Throwable t) {
        }
    }

    public static CaptureDesktop getInstance() {
        if(instance == null) {
            instance = new CaptureDesktop();
        }
        return instance;
    }

    public void init() {
        videoCapture = VideoCapture.create();
        User user = User.getInstance();
        String id = user.getId();

        videoCapture.setVideoSource(new Desktop());
        List<Codec> videoCodecs = videoCapture.getVideoCodecs();
        Codec videoCodec = videoCodecs.get(0);

        String videoExtension = (String) videoCapture.getVideoFormat().getId();
        String videoFileName = fileManager.generateFileName(id + "." + videoExtension);
        videoFile = new File(videoFileName);

        EncodingParameters encodingParameters = new EncodingParameters(videoFile);
        encodingParameters.setSize(new Dimension(800, 600));
        encodingParameters.setBitrate(800000);
        encodingParameters.setFramerate(10);
        encodingParameters.setCodec(videoCodec);
        encodingParameters.setKeyFrameInterval(1000);
        encodingParameters.setCompressionQuality(CompressionQuality.BEST);
        videoCapture.setEncodingParameters(encodingParameters);
    }

    public void start() {
        try {
            videoCapture.start();
            fileManager.trackSystemFile(videoFile);
        } catch(Throwable t) {
        }
    }

    public void stop() {
        try {
            videoCapture.stop();
        } catch(Throwable t) {
        }
    }
}
