package kasirgalabs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionOnOff implements Runnable{
    /*
     * IP adresini sil. Internet ve bluetooth ile iletisimi keser.
     */
    public static void closeConnections() {
        String[] command = {"cmd.exe", "/c", "ipconfig/release"};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        }
        catch(IOException ex) {
            Logger.getLogger(ConnectionOnOff.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    /*
     * IP adresi al. Internet ve bluetooth calisir duruma gecer.
     */
    public static void openConnections() {
        String[] command = {"cmd.exe", "/c", "ipconfig/renew"};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        }
        catch(IOException ex) {
            Logger.getLogger(ConnectionOnOff.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @Override
    public void run() {
        closeConnections();
    }
}
