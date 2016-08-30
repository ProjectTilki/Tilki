import java.io.File;
import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        FoxClientFileManager fcfm = new FoxClientFileManager();
        File[] files = new File[1];
        files[0] = new File("test0.txt");
        fcfm.createZipFile(files);
        fcfm.sendFile("test0.zip", "1231", "Exam1");
    }
}
