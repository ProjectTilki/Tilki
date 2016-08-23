import java.io.IOException;

/**
 * Demo class for FoxClient class.
 */
public class FoxClientDemo {

    public static void main(String[] args) throws IOException {
        try {
            FoxClient fc = new FoxClient();
            int ogrenciNumarasi = 0;
            String dosyaAdi = "jdk-8u102-linux-x64.tar.gz";
            fc.connectToServer("gozetmenSifresi", "ogrenciAdi", "ogrenciSoyadi",
                               ogrenciNumarasi, dosyaAdi);
        } catch(FoxException fe) {
            fe.printStackTrace();
        }
    }
}
