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
        files[0] = new File("test");
        fcfm.createZipFile(files);
    }
}
