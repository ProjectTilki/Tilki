
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) {
        /*FoxClientEnrollment fce = new FoxClientEnrollment();
        String status = fce.enroll("name", "surname", "0", "Exam1", "key");
        System.out.println(status);*/
        
        FoxClientExamManager fcem = new FoxClientExamManager();
        Exam exams[] = null;
        try {
            exams = fcem.availableExams();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < exams.length; i++)
            System.out.println(exams[i].getName() + " " + exams[i].getDescription());
    }
}
