package com.kasirgalabs.tilki.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class KeyVerifyRefactored extends DefaultService<ServiceListener<Boolean>, Boolean, char[]> {

    public KeyVerifyRefactored(ArrayList<ServiceListener<Boolean>> listeners) {
        super(listeners);
    }

    @Override
    public Boolean call() throws Exception {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 50101);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("KeyVerifyRefactored");
            out.writeUTF("deneme");
            for(char c : data) {
                out.writeChar(c);
            }
            out.flush();
            socket.shutdownOutput();
            if(in.readBoolean()) {
                return true;
            }
        }
        catch(IOException ex) {
            throw ex;
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
        return false;
    }
}
