
import java.io.File;
import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException {        
        FoxClientEnrollment fce = new FoxClientEnrollment();
        System.out.println(fce.enroll("name", "surname", "123123", "Exam1"));
        System.out.println(fce.verifyInstructorKey("keys"));
    }
}
