import java.io.File;
import java.io.IOException;

/**
 * Demo class for FoxZip class.
 */
public class FoxZipDemo {

    public static void main(String[] args) throws IOException {
        FoxZip fz = new FoxZip();
        File[] files = new File[3];
        files[0] = new File("front.c");
        files[1] = new File("odev3.pdf");
        files[2] = new File("soru1.c");
        File zipFile = fz.createZipFile(files);
        System.out.println(zipFile.getName());
    }
}
