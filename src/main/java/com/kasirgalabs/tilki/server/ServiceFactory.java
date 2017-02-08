package com.kasirgalabs.tilki.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceFactory {

    private final Socket socket;

    public ServiceFactory(Socket socket) {
        this.socket = socket;
    }

    public Service getService(String service) {
        try {
            if(service.equals("Check in.")) {
                return new CheckIn(socket);
            }
            else if(service.equals("Key verify.")) {
                return new KeyVerify(socket);
            }
            else if(service.equals("Sending file.")) {
                return new FileSaver(socket);
            }
            else if(service.equals("List exams.")) {
                return new GetExams(socket);
            }
            else if(service.equals("Sending error logs.")) {
                return new LogErrors(socket);
            }
            else if(service.equals("KeyVerifyRefactored")) {
                return new KeyVerifyRefactored(socket);
            }
        }
        catch(IOException ex) {
            Logger.getLogger(ServiceFactory.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return null;
    }
}
