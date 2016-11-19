package kasirgalabs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is used to log and send exceptions that are occurred.
 */
public class ClientExceptionHandler {

    /**
     * Appends an exception stack trace to a file named "error.log". If the file
     * does not exists will be created in the current working directory. If a
     * security manager exists and its checkWrite method denies write access to
     * the file exception will not be written.
     *
     * @param e Exception to be appended.
     */
    public static synchronized void logAnException(Exception e) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream("error.log", true));
        }catch(IOException ex) {
        }finally {
            if(pw != null)
                pw.close();
        }
    }

    /**
     * Sends the exceptions that are listed in the "error.log" file to a
     * predefined host. If the file does not exists, does nothing.
     * <p>
     * Exceptions occurred in this method are ignored and won't be reported.
     */
    public static void sendExceptionsToServer() {
        BufferedReader fileIn = null;
        Socket socket = null;
        try {
            File errorLogFile = new File("error.log");
            if(errorLogFile.exists() && !errorLogFile.isDirectory())
                fileIn = new BufferedReader(new FileReader(errorLogFile));
            else
                return;

            BufferedReader reader = new BufferedReader(new FileReader("error.log"));
            int lines = 0;
            while (reader.readLine() != null)
                lines++;
            reader.close();
            socket = new Socket(MainClient.getIpAddress(), 50101);
            DataOutputStream socketOut = new DataOutputStream(socket.
                    getOutputStream());

            socketOut.writeUTF("Sending error logs.");
            socketOut.writeInt(lines);
            System.out.println(lines);
            String line;
            while((line = fileIn.readLine()) != null)
                socketOut.writeUTF(line);
            socketOut.writeUTF(null);
        }catch(Exception ex0) {
        }finally {
            try {
                if(fileIn != null)
                    fileIn.close();
                if(socket != null)
                    socket.close();
            }catch(Exception ex1) {
            }
        }
    }
}
