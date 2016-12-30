package kasirgalabs;

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
        if(service.equals("Check in.")) {
            try {
                return new CheckIn(socket);
            }
            catch(IOException ex) {
                Logger.getLogger(ServiceFactory.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
