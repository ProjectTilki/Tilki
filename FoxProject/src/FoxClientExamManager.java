import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class FoxClientExamManager {
    
    /**
     * Fetches the available exam list from a predefined server.
     * 
     * @return Each element of the array list represents an exam's name.
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public Exam[] availableExams() throws ClassNotFoundException, IOException{
        Socket socket = new Socket("localhost", 50101);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        out.println("List exams.");
        out.flush();
        
        Object examListObject = ois.readObject();        
        Exam[] examList = (Exam[]) examListObject;
        return examList;
    }
}
