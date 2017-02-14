package com.kasirgalabs.tilki.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.concurrent.Task;

/*
 * This service sends the exam key to the server and ask if it's matches with
 * the original exam key than notifies the registered listeners if there is any.
 */
public class KeyVerifyRefactored extends Task<Boolean> {

    @Override
    public Boolean call() throws Exception {
        Socket socket = null;
        try {
            User user = User.getInstance();
            if(user.getExam() == null || user.getExam().getKey() == null) {
                return false;
            }

            socket = new Socket("localhost", 50101);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Tell the server which service we want it to perform.
            out.writeUTF("KeyVerifyRefactored");
            // Send exam name.
            out.writeUTF("deneme");
            // Send the typed key.
            for(char c : user.getExam().getKey()) {
                out.writeChar(c);
            }
            out.flush();
            socket.shutdownOutput(); // Send EOF since no more data will be sent.
            return in.readBoolean(); // Return the response of the server.
        } catch(IOException ex) {
            //Logger.getLogger(KeyVerifyRefactored.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
}
