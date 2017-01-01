package com.kasirgalabs.tilki.client;

import javax.swing.Timer;

public class SimpleTimer extends Timer {

    public SimpleTimer(int delay, SimpleTimerActionListener listener) {
        super(delay, listener);
    }

}
