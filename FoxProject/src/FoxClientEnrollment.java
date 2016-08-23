import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoxClientEnrollment {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public FoxClientEnrollment() {
        try {
            socket = new Socket("10.5.146.7", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String Enroll(String name, String surname, String id, String examCode, String instructorKey) {
        out.println("Enrollment.");
        out.println(name);
        out.println(surname);
        out.println(id);
        out.println(examCode);
        out.println(instructorKey);
        if(out.checkError())
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, "IOException");
        String status = null;
        try {
            status = in.readLine();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status != null)
            if(status.equals("0"))
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.INFO, "Enrollment is accepted.");
            else if(status.equals("1"))
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.INFO, "Instructor key is not accepted.");
            else if(status.equals("2"))
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.INFO, "Connection is recovered.");
            else
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, "An error has occured.");
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
}
