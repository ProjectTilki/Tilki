package com.kasirgalabs.tilki.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JComponent;
import javax.swing.Timer;

public abstract class DefaultService<T, V extends JComponent> implements
        Callable<T>, Service<T> {

    protected final Socket socket;
    protected final DataInputStream in;
    protected final DataOutputStream out;
    private DefaultActionListener<V> defaultActionListener;
    ExecutorService executor = Executors.newFixedThreadPool(2);
    private Future<T> future;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     *
     * @throws java.io.IOException If an IO error occurs on socket's streams.
     */
    protected DefaultService(Socket socket,
            DefaultActionListener<V> defaultActionListener) throws IOException {
        this.defaultActionListener = defaultActionListener;
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public final void request() {
        future = executor.submit(DefaultService.this);
        executor.submit(new ServiceListener());
        //future = executor.submit(this);
    }

    @Override
    public final T getResult() throws Exception {
        return future.get();
    }

    private class ServiceListener implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            while(!future.isDone());
            Timer timer = new Timer(100, defaultActionListener);
            timer.setRepeats(false);
            timer.start();
            return null;
        }
    }
}
