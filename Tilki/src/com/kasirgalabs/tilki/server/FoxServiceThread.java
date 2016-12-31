package com.kasirgalabs.tilki.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoxProject server side utilities. This class consists of methods that operate
 * on client requests. Main purpose of this class log the exam attendance
 * request and save incoming files.
 */
public class FoxServiceThread implements Callable<Integer> {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     *
     * @throws java.io.IOException If an IO error occurs on socket's streams.
     */
    public FoxServiceThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Thread starts execution on this method.
     * <p>
     * Reads data from an incoming connection and decides what to do. This
     * method returns if and only if operation is successful.
     *
     * @return 1 if operation is successful.
     *
     * @throws Exception if unable to compute the result.
     */
    @Override
    public Integer call() throws Exception {
        try {
            String data = in.readUTF(); // Read the requested operation.
            ServiceFactory serviceFactory = new ServiceFactory(socket);
            Service service = serviceFactory.getService(data);
            if(service != null) {
                service.serve();
            }
        }
        catch(IOException | NoSuchAlgorithmException e) {
            Logger.getLogger(FoxServer.class.getName()).log(Level.SEVERE,
                    "Error during session.", e);
        }
        finally {
            socket.close();
        }
        return 1;
    }
}
