package com.kasirgalabs.tilki.client;

import java.awt.Container;
import java.util.logging.Level;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Tilki {

    private static MainScreen mainScreen;

    public static void main(String[] args) {
        /*
         * Use an appropriate Look and Feel
         */
        try {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }

        });
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        mainScreen = new MainScreen("Tilki");
        mainScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Tilki tilki = new Tilki();
        tilki.addComponentToPane(mainScreen.getContentPane());

        //Display the window.
        mainScreen.pack();
        mainScreen.setResizable(false);
        mainScreen.setLocationRelativeTo(null);
        mainScreen.setVisible(true);
    }

    private void addComponentToPane(Container contentPane) {
        MainScreenLayout mainScreenLayout = new MainScreenLayout();
        mainScreen.setLayout(mainScreenLayout);
        LoginPanel loginPanel = new LoginPanel(mainScreen);
        ExamPanel examPanel = new ExamPanel(mainScreen);
        ConfirmationPanel confirmationPanel = new ConfirmationPanel(mainScreen);
        CaptureDesktopPanel screenCapturePanel = new CaptureDesktopPanel();

        contentPane.setLayout(mainScreenLayout);
        contentPane.add(loginPanel, "card0");
        contentPane.add(examPanel, "card1");
        contentPane.add(confirmationPanel, "card2");
        contentPane.add(screenCapturePanel, "card3");
    }
}
