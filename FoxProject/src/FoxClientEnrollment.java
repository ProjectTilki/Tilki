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

    /**
     * Constructor for initializing instance variables.
     */
    public FoxClientEnrollment() {
        try {
            socket = new Socket("localhost", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Enrolls a student.
     * @param name student name
     * @param surname student surname
     * @param id student id
     * @param exam exam name
     * @param instructorKey instructor key for authorization step.
     * @return returns the status of the enrollment; returns 0 if enrollment accepted, 1 if authorization is failed,
     * 2 if connection is recovered, returns null if an error occurred.
     */
    public String enroll(String name, String surname, String id, String exam, String instructorKey) {
        out.println("Enrollment.");
        out.println(name);
        out.println(surname);
        out.println(id);
        out.println(exam);
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
            else if(status.equals("3"))
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, "Exam not found.");
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
}
