package com.kasirgalabs.tilki.client;

import java.awt.EventQueue;

public class MainScreen extends javax.swing.JFrame {

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        catch(InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        catch(IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        catch(javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }

    private final LoginPanel loginPanel;
    private final ExamPanel examPanel;
    private final MainScreenLayout mainScreenLayout;
    private User user;

    public MainScreen() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        mainScreenLayout = new MainScreenLayout();
        setLayout(mainScreenLayout);

        loginPanel = new LoginPanel(this);
        examPanel = new ExamPanel(this);
        add(loginPanel, "card0");
        add(examPanel, "card1");

        pack();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(User user) {
        return user;
    }

    public void nextScreen() {
        mainScreenLayout.next(getContentPane());
        pack();
    }

    public void previousScreen() {
        mainScreenLayout.previous(getContentPane());
        pack();
    }
}
