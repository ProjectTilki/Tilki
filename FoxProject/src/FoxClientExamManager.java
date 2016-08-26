import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class FoxClientExamManager {
    
    /**
     * Fetches the available exam list from a predefined server.
     * 
     * @return Each element of the array list represents an exam. Returns null if there is no exam.
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public Exam[] availableExams() throws ClassNotFoundException, IOException{
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
