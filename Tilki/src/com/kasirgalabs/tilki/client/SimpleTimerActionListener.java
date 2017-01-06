package com.kasirgalabs.tilki.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class SimpleTimerActionListener implements ActionListener {

    private final long currentTime;
    private final JLabel component;

    public SimpleTimerActionListener(JLabel component) {
        super();
        this.component = component;
        this.currentTime = System.currentTimeMillis();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        component.setText(getElapsedTime());
    }

    private String getElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - currentTime;

        elapsedTime /= 1000;
        String seconds = Integer.toString((int) (elapsedTime % 60));
        String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
        String hours = Integer.toString((int) (elapsedTime / 3600));

        seconds = addPrefixZero(seconds);
        minutes = addPrefixZero(minutes);
        hours = addPrefixZero(hours);

        return hours + ":" + minutes + ":" + seconds;
    }

    private String addPrefixZero(String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }
}
