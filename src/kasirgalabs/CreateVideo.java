package kasirgalabs;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CreateVideo {

    private static final int FRAME_RATE = 1;

    private static final String outputFilename = "webcam.wmv";

    public static void createVideo(ArrayList<BufferedImage> imgarry) {

        int width = imgarry.get(0).getWidth();
        int height = imgarry.get(0).getHeight();

        final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);

        // We tell it we're going to add one video stream, with id 0, at position 0
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_WMV2, width, height);

        long time = 0;
        for(int index = 0; index < imgarry.size(); index++) {
            // encode the image to stream #0 
            writer.encodeVideo(0, imgarry.get(index), time,
                    TimeUnit.MILLISECONDS);
            time += 1000 / FRAME_RATE;
        }
        writer.close();
    }
}
