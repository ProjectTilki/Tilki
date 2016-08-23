import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoxClientExamManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public FoxClientExamManager() {
        try {
            socket = new Socket("10.5.146.7", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] availableExams() {
        out.println("List exams.");
        ArrayList<String> examList = new ArrayList<String>(0);
        String exam;
        try {
            while((exam = in.readLine()) != null)
                examList.add(exam);
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(examList.size() > 0)
            return (String[]) examList.toArray();
        else
            return null;
    }

    public String examDescription(String exam) {
        out.println("Exam description.");
        StringBuilder examDescription = new StringBuilder();
        String data;
        try {
            while((data = in.readLine()) != null) {
                data = in.readLine();
                examDescription.append(data);
            }
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return examDescription.toString();
    }
}
