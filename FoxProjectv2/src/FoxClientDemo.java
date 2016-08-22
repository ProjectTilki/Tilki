import java.io.IOException;

/**
 * Demo class for FoxClient class.
 */
public class FoxClientDemo {
    public static void main(String[] args) throws IOException {
        try {
            FoxClient fc = new FoxClient();
            int ogrenciNumarasi = 0;
            String dosyaAdi = "documents-export-2016-06-17.zip";
            fc.connectToServer("gozetmenSifresi", "ogrenciAdi", "ogrenciSoyadi", ogrenciNumarasi, dosyaAdi);
        } catch(FoxException fe) {
            fe.printStackTrace();
        }
    }
}
