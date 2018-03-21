package kasirgalabs;

import java.awt.EventQueue;
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
                for(;;) {
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
                          
                        System.out.println("TAKE PİCTURE");
                        imgarry.add(videoCap.getOneFrame());
                        try {
                            Thread.sleep(1000);// 10 dk
                        }
                        catch(InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
public void stop(){
   
    CreateVideo.createVideo(imgarry);
    setVisible(false);
    
    
}

}
