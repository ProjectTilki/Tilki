/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirgalabs;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author rootg
 */
public class LogErrors implements Service {

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
    public LogErrors(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public synchronized void serve() throws IOException {
        BufferedWriter fileOut = null;
        try {
            int lineCount = in.readInt();
            File f = new File("clientErrors.log");
            fileOut = new BufferedWriter(new FileWriter(f, true));
            String line;
            while(lineCount > 0) {
                line = in.readUTF();
                fileOut.write(line + "\n");
                lineCount--;
            }
        }
        catch(IOException ex) {
            throw ex;
        }
        finally {
            if(fileOut != null) {
                fileOut.close();
            }
        }
    }

}
