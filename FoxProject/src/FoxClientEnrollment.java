import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FoxClientEnrollment {
    
    /**
     * Enrolls a student.
     * @param name student name
     * @param surname student surname
     * @param id student id
     * @param exam exam name
     * @param instructorKey instructor key for authorization step.
     * @return returns the status of the enrollment; returns 0 if exam key file is missing, 1 if reconnection is successful,
     * 2 if enrollment is done, 3 if instructor key is not accepted. Other return values indicate an error.
     * @throws java.io.IOException
     */
    public int enroll(String name, String surname, String id, String exam, String instructorKey) throws IOException {
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
        if(status != null) {
            char c = status.charAt(0);
            if((c == '0' || c == '1' || c == '2' || c == '3') && status.length() == 1)
                return Integer.parseInt(status);
        }
        return -1;
    }
}
