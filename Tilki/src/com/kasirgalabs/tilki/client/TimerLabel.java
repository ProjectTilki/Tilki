package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.TilkiColor;
import javax.swing.JLabel;

public class TimerLabel extends JLabel {

    private static volatile TimerLabel instance = null;

    public static TimerLabel getInstance() {
        if(instance == null) {
            synchronized(TimerLabel.class) {
                if(instance == null) {
                    instance = new TimerLabel();
                    instance.setForeground(TilkiColor.BLUE);
                }
            }
        }
        return instance;
    }

    private TimerLabel() {

    }
}
