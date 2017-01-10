package com.kasirgalabs.tilki.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SimpleTimerActionListener implements
        ActionListener {

    private static volatile SimpleTimerActionListener instance = null;

    private final long currentTime;
    private final ArrayList<Observer<String>> observers;

    private SimpleTimerActionListener() {
        this.currentTime = System.currentTimeMillis();
        observers = new ArrayList<>();

    }

    public static SimpleTimerActionListener getInstance() {
        if(instance == null) {
            synchronized(SimpleTimerActionListener.class) {
                if(instance == null) {
                    instance = new SimpleTimerActionListener();
                }
            }
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(observers == null || observers.isEmpty()) {
            return;
        }
        for(Observer<String> observer : observers) {
            observer.update(getElapsedTime());
        }
    }

    public <E extends Observer<String>> void addObserver(E observer) {
        observers.add(observer);
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
