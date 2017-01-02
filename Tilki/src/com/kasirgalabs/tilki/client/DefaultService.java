package com.kasirgalabs.tilki.client;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.Timer;

public abstract class DefaultService<E extends ServiceListener<V>, V> implements
        Callable<V>, Service<V> {

    private final ArrayList<E> listeners;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private Future<V> future;

    protected DefaultService(ArrayList<E> listeners) {
        this.listeners = listeners;
    }

    @Override
    public final void request() {
        future = executor.submit(this);
        executor.submit(new ServiceWaiter());
    }

    private class ServiceWaiter implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            while(!future.isDone());
            ServiceResultDispatcher<E, V> serviceResultDispatcher = new ServiceResultDispatcher<>(
                    listeners);
            serviceResultDispatcher.setResult(future);
            Timer timer = new Timer(100, serviceResultDispatcher);
            timer.setRepeats(false);
            timer.start();
            return null;
        }
    }
}
