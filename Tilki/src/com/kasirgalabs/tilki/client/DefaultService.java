package com.kasirgalabs.tilki.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class DefaultService<E> implements Callable<E>, Service<E> {

    protected final Socket socket;
    protected final DataInputStream in;
    protected final DataOutputStream out;
    private Future<E> future;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     *
     * @throws java.io.IOException If an IO error occurs on socket's streams.
     */
    protected DefaultService(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public final void request() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        future = executor.submit(this);
    }

    @Override
    public final E getResult() throws Exception {
        return future.get();
    }
}
