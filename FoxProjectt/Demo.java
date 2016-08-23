
/**
 *
 * Demo class for FoxClient class.
 */
public class Demo {

    public static void main(String[] args) {
        try {
            FoxClient fc = new FoxClient();
            fc.connectToServer("test0", "test1", "test2", 0, "C:\\Users\\Erhan\\Desktop\\Deneme_2Agustos(5).WMV");
        }catch(FoxException fe) {
            fe.printStackTrace();
        }
    }
}
