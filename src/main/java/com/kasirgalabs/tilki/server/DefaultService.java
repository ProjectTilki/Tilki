package com.kasirgalabs.tilki.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class DefaultService implements Service {

    protected final Socket socket;
    protected final DataInputStream in;
    protected final DataOutputStream out;

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
}
