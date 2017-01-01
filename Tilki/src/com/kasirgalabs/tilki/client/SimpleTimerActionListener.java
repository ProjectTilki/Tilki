package com.kasirgalabs.tilki.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class SimpleTimerActionListener implements ActionListener {

    private final JLabel label;
    private final long currentTime;

    public SimpleTimerActionListener(JLabel label) {
        this.label = label;
        this.currentTime = System.currentTimeMillis();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        label.setText(getElapsedTime());
    }

    private String getElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - currentTime;

        elapsedTime /= 1000;
        String seconds = Integer.toString((int) (elapsedTime % 60));
        String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
        String hours = Integer.toString((int) (elapsedTime / 3600));

        if(seconds.length() < 2) {
            seconds = "0" + seconds;
        }

        if(minutes.length() < 2) {
            minutes = "0" + minutes;
        }

        if(hours.length() < 2) {
            hours = "0" + hours;
        }

        return hours + ":" + minutes + ":" + seconds;
    }
}
