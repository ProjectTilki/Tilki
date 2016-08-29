import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class contains methods for connecting to a predefined host and sending
 * informations. Methods in this class can be used to verify the name, surname
 * and id for an exam attendance of a student.
 * <p>
 * All the methods in this class uses {@link java.net.Socket} object to connect
 * to a host. The methods of this class all throw {@link java.io.IOException} if
 * an I/O error occurs on sockets.
 */
public class FoxClientEnrollment {
    private String name = null;
    private String surname = null;
    private String id = null;
    private String exam = null;

    /**
     * Check in a student or establish a reconnection to a predefined host. This
     * method uses {@link java.net.Socket} class to connect to a host.
     * <p>
     * Method parameters are saved in instance variables for further use.
     *
     * @param name    The student name.
     * @param surname The student surname.
     * @param id      The student id.
     * @param exam    The exam name.
     *
     * @return The status of the check in; returns 0 if reconnection successful,
     *         1 if check in is successful, returns -1 if an error occurred.
     *
     * @throws java.io.IOException If an I/O error occurs when creating socket,
     *                             reading from socket, or sending to socket.
     */
    public int checkIn(String name, String surname, String id, String exam)
            throws IOException {
        // Save informations to instance variables.
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.exam = exam;

        Socket socket = new Socket("localhost", 50101);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Check in."); // Tell host which operation will occur.

        // Send informations to the host.
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.flush();

        String status = in.readUTF(); // Read status code of check in.
        if(status != null) {
            char c = status.charAt(0);
            return Character.getNumericValue(c);
        }
        socket.close();
        return -1;
    }

    /**
     * Verifies the enrollment of a student by connecting to a predefined host
     * This method uses {@link java.net.Socket} object to connect to the host.
     * <p>
     * Implementation note: Student informations are stored in instance
     * variables. Before using this method, checkIn method must be used to store
     * them. If not, a {@link java.lang.NullPointerException} will be thrown.
     *
     * @param instructorKey The instructor key
     *
     * @return Returns 0 if key file is missing, returns 1 if key is not
     *         accepted, returns 2 if key is accepted, returns -1 if an error is
     *         occurred.
     *
     * @throws java.io.IOException If an I/O error occurs when creating socket,
     *                             reading from socket, or sending to socket.
     */
    public int verifyInstructorKey(String instructorKey) throws IOException {
        if(name == null || surname == null || id == null || exam == null || instructorKey == null)
            throw new NullPointerException();
        Socket socket = new Socket("localhost", 50101);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Key verify."); // Tell host which operation will occur.

        // Send informations.
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.writeUTF(instructorKey);
        out.flush();

        String status = in.readUTF(); // Read the status code.
        if(status != null) {
            char c = status.charAt(0);
            return Character.getNumericValue(c);
        }
        socket.close();
        return -1;
    }
}
