
import java.io.File;
import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException {        
        FoxClientFileManager fcfm = new FoxClientFileManager();
        File[] files = new File[2];
        files[0] = new File("Lab4_foy_2016_1.doc");
        files[1] = new File("Exam1");
        fcfm.createZipFile(files);
    }
}
