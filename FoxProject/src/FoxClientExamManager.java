import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class FoxClientExamManager {

    /**
     * Requests the available exam list from a predefined host.
     *
     * @return Each element of the array list represents an exam. Returns null
     *         if there is no exam.
     *
     * @throws java.lang.ClassNotFoundException Class of a serialized object
     *                                          cannot be found.
     * @throws java.io.IOException              Any of the usual I/O related
     *                                          exceptions.
     * @see Exam
     */
    public Exam[] availableExams() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 50101);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("List exams.");
        out.flush();

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object examListObject = ois.readObject();
        Exam[] examList = (Exam[]) examListObject;
        return examList;
    }
}
