import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoxClientExamManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream ois;
    /**
     * Fetches the available exam list from a predefined server.
     * 
     * @return Each element of the array list represents an exam's name.
     */
    public Exam[] availableExams() throws ClassNotFoundException, IOException{
            socket = new Socket("localhost", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ois = new ObjectInputStream(socket.getInputStream());
        out.println("List exams.");
        out.flush();
        
        Object examListObject = null;
            examListObject = ois.readObject();        
        Exam[] examList = (Exam[]) examListObject;
        return examList;
    }
}
