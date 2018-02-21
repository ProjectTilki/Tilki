package kasirgalabs;

public class ConnectionOnOff {
    /*
     * IP adresini sil. Internet ve bluetooth ile iletisimi keser.
     */
    public static void closeConnections() throws Exception {
        String[] command = {"cmd.exe", "/c", "ipconfig/release"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
    }
    /*
     * IP adresi al. Internet ve bluetooth calisir duruma gecer.
     */
    public static void openConnections() throws Exception {
        String[] command = {"cmd.exe", "/c", "ipconfig/renew"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
    }
}
