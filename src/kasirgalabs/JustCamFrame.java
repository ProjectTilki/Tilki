package kasirgalabs;

import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.opencv.core.Mat;

public class JustCamFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public JustCamFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                        //takePicture(videoCap.getOneFrame());
                        try {
                            Thread.sleep(600000);// 10 dk
                        }
                        catch(InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                Thread t2 = new Thread(takePhoto);
                t2.start();
    }

    JustCam videoCap = new JustCam();

    public void paint(Graphics g) {
        g = contentPane.getGraphics();
        g.drawImage(videoCap.getOneFrame(), 0, 0, this);
    }

}
