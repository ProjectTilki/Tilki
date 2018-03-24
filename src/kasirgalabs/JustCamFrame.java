package kasirgalabs;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.opencv.core.Mat;

public class JustCamFrame extends JFrame implements Runnable {

    private JPanel contentPane;
    ReportWriting rw = new ReportWriting();
    TrustScore justFaceScore = new TrustScore();
    static boolean durdur = false;
    static ArrayList<BufferedImage> imgarry = new ArrayList<BufferedImage>();
    private ScheduledExecutorService timer;

    /**
     * Create the frame.
     */
    public void run() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLocationRelativeTo(null);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        Runnable repaint = new Runnable() {
            @Override
            public void run() {
                while(!durdur) {
                    repaint();
                    try {
                        Thread.sleep(30);
                    }
                    catch(InterruptedException e) {
                    }
                }
            }
        };
        Thread t1 = new Thread(repaint);
        t1.start();
        Runnable takePhoto = new Runnable() {
            @Override

            public void run() {
                   int i=0;
                while(!durdur) {
                    //justFaceScore.skorArttir(2);
                    //rw.addText("Couldn't find face ",0);
                    BufferedImage img=videoCap.getOneFrame();
                    System.out.println("TAKE PİCTURE");
                    File outputfile = new File("image"+i+++".jpg");
                    try {
                        ImageIO.write(img, "jpg", outputfile);
                    }
                    catch(IOException ex) {
                        Logger.getLogger(JustCamFrame.class.getName()).log(
                                Level.SEVERE,
                                null, ex);
                    }

                    imgarry.add(img);
                    try {
                        Thread.sleep(600000);// 10 dk
                    }
                    catch(InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t2 = new Thread(takePhoto);
        t2.start();
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    JustCam videoCap = new JustCam();

    public void paint(Graphics g) {
        g = contentPane.getGraphics();
        g.drawImage(videoCap.getOneFrame(), 0, 0, this);
    }

    public void stop() {
        System.out.println("kasirgalabs.JustCamFrame.stop()");
        durdur = true;
        CreateVideo.createVideo(imgarry);
        setVisible(false);

    }

    public int getJustFaceScore() {
        return justFaceScore.getSkor();
    }

}
