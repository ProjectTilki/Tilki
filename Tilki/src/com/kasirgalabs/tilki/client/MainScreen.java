package com.kasirgalabs.tilki.client;

import javax.swing.JFrame;

public class MainScreen extends JFrame {

    private MainScreenLayout mainScreenLayout;
    private User user;

    public MainScreen(String title) {
        super(title);
    }

    public void setLayout(MainScreenLayout mainScreenLayout) {
        this.mainScreenLayout = mainScreenLayout;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void nextScreen() {
        mainScreenLayout.next(getContentPane());
        centerWindow();
    }

    public void previousScreen() {
        mainScreenLayout.previous(getContentPane());
        centerWindow();
    }

    private void centerWindow() {
        pack();
        setLocationRelativeTo(null);
    }
}
