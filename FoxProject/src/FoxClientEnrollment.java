import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class FoxClientEnrollment {
    private String name;
    private String surname;
    private String id;
    String exam;
    
    /**
     * Enrolls a student or establishes a reconnection.
     * Student informations are saved in instances variables.
     * @param name student name
     * @param surname student surname
     * @param id student id
     * @param exam exam name
     * @return returns the status of the enrollment; returns 0 if reconnection successful, 1 if check in is successful,
     * returns -1 if an error occurred.
     * @throws java.io.IOException
     */
    public int enroll(String name, String surname, String id, String exam) throws IOException {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.exam = exam;
        Socket socket = new Socket("localhost", 50101);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.writeUTF("Enrollment.");
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        String status = in.readLine();
        if(status != null && status.length() > 2) {
            char c = status.charAt(2);
            return Character.getNumericValue(c);
        }
        socket.close();
        return -1;
    }
    /**
     * Verifies the enrollment of a student by connecting to server.
     * Implementation note: Before using this method, enroll method should be used.
     * @param instructorKey instructor key
     * @return returns 0 if key file is missing, returns 1 if key is not accepted,
     * returns 2 if key is accepted, returns -1 if an error is occurred.
     * @throws IOException 
     */
    public int verifyInstructorKey(String instructorKey) throws IOException {
        Socket socket = new Socket("localhost", 50101);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("Key verify.");
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.writeUTF(instructorKey);
        out.flush();
        String status = in.readUTF();
        if(status != null) {
            char c = status.charAt(0);
            if((c == '0' || c == '1' || c == '2') && status.length() == 1) {
                if(Integer.parseInt(status) == 0)
                    return 0;
                else if(Integer.parseInt(status) == 1)
                    return 1;
                else
                    return 2;
            }
        }
        socket.close();
        return -1;
    }
}
