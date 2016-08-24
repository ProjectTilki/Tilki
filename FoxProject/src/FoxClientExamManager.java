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
    public Exam[] availableExams() {
        try {
            socket = new Socket("localhost", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ois = new ObjectInputStream(socket.getInputStream());
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("List exams.");
        
        Object examListObject = null;
        try {
            examListObject = ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Exam[] examList = (Exam[]) examListObject;
        return examList;
    }
}
