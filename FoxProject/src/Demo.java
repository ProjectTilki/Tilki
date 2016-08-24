/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) {
        FoxClientEnrollment fce = new FoxClientEnrollment();
        String status = fce.enroll("name", "surname", "0", "TestExam", "key");
        
        FoxClientExamManager fcem = new FoxClientExamManager();
        String exams[] = fcem.availableExams();
        for(int i = 0; i < exams.length; i++)
            System.out.println(exams[i]);
        
        String exam = fcem.examDescription("TestExam");
        System.out.println(exam);
    }
}
