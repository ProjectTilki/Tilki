import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FoxClientUtilities fcu = new FoxClientUtilities();
        System.out.println(fcu.checkIn("asd", "surname", "id", "aawdawdawd"));
        System.out.println(fcu.verifyInstructorKey("asdasd"));

    }
}
