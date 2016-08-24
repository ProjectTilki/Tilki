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

    /**
     * Fetches the available exam list from a predefined server.
     * 
     * @return Each element of the array list represents an exam's name.
     */
    public String[] availableExams() {
        try {
            socket = new Socket("10.5.146.7", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("List exams.");
        ArrayList<String> examList = new ArrayList<String>(0);
        String exam;
        try {
            while((exam = in.readLine()) != null) {
                if(exam.equals("Done."))
                    break;
                examList.add(exam);
            }
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String exams[] = new String[examList.size()];
        if(examList.size() > 0) {
            for(int i = 0; i < examList.size(); i++)
                exams[i] = examList.get(i);
            return  exams;
        }
        else
            return null;
    }

    /**
     * Method for fetching exam description specified with the exam parameter.
     * 
     * @param exam exam name to be fetched.
     * @return description of an exam specified with the exam parameter. If exam does not exist,
     * behavior is not defined.
     */
    public String examDescription(String exam) {
        try {
            socket = new Socket("10.5.146.7", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("Exam description.");
        out.println(exam);
        StringBuilder examDescription = new StringBuilder();
        String data;
        try {
            while((data = in.readLine()) != null) {
                if(data.equals("Done."))
                    break;
                examDescription.append(data);
            }
        } catch(IOException ex) {
            Logger.getLogger(FoxClientExamManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return examDescription.toString();
    }
}
