package com.kasirgalabs.tilki.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class ServiceResultDispatcher<E extends ServiceListener<V>, V> implements
        ActionListener {

    private final ArrayList<E> listeners;
    private Future<V> future;

    public ServiceResultDispatcher(ArrayList<E> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(E listener : listeners) {
            listener.servicePerformed(future);
        }
    }

    public void setResult(Future<V> future) {
        this.future = future;
    }
}
