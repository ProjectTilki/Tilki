
import java.io.File;
import java.io.IOException;

/**
 *
 * Demo class for testing.
 */
public class Demo {

    public static void main(String[] args) throws IOException {        
        File[] files = new File[3];
        files[0] = new File("10101111_asd.txt");
        files[1] = new File("Get Started with Dropbox.pdf");
        files[2] = new File("Lab4_foy_2016_1.doc");
        
        FoxClientFileManager fcfm = new FoxClientFileManager();
        String zipFileName = fcfm.createZipFile(files);
        String checksum = fcfm.sendFile(zipFileName, "20202222", "Exam1");
        System.out.println(checksum);
    }
}
