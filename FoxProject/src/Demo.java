import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FoxClientUtilities fcu = new FoxClientUtilities();
        Exam[] exam = fcu.availableExams();
        if(exam != null)
            for(int i = 0; i < exam.length; i++)
                System.out.println(exam[i].getName() + "\n" + exam[i].
                        getDescription() + "\n");
    }
}
