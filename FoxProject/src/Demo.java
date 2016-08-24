/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) {
        FoxClientEnrollment fce = new FoxClientEnrollment();
        String status = fce.enroll("name", "surname", "0", "Exam1", "key");
        System.out.println(status);
        
        FoxClientExamManager fcem = new FoxClientExamManager();
        Exam exams[] = fcem.availableExams();
        for(int i = 0; i < exams.length; i++)
            System.out.println(exams[i].getName() + " " + exams[i].getDescription());
    }
}
