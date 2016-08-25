import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class FoxClientEnrollment {
    
    /**
     * Enrolls a student.
     * @param name student name
     * @param surname student surname
     * @param id student id
     * @param exam exam name
     * @param instructorKey instructor key for authorization step.
     * @return returns the status of the enrollment; returns 0 if enrollment accepted, 1 if authorization is failed,
     * 2 if connection is recovered, returns null if an error occurred.
     * @throws java.io.IOException
     */
    public String enroll(String name, String surname, String id, String exam, String instructorKey) throws IOException {
        Socket socket = new Socket("localhost", 50101);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("Enrollment.");
        out.println(name);
        out.println(surname);
        out.println(id);
        out.println(exam);
        out.println(instructorKey);
        String status = in.readLine();
        socket.close();
        return status;
    }
}
