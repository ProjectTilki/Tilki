package com.kasirgalabs.tilki.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class LogErrors extends DefaultService {

    public LogErrors(Socket socket) throws IOException {
        super(socket);
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
