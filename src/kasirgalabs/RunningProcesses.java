package kasirgalabs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import com.google.common.collect.Sets;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daghan
 */
public class RunningProcesses extends JFrame implements Runnable {

    public static JFrame frame3 = null;
    List<String> kapatilanUygulamalar = new ArrayList<String>();
    List<String> acilanUygulamalar = new ArrayList<String>();
    List<String> blockedAppsList = new ArrayList<String>();
    ReportWriting rw = new ReportWriting();
    TrustScore ts;
    boolean internetiKapat;

    public List<String> getKapatilan() {
        return this.kapatilanUygulamalar;
    }

    public List<String> getAcilan() {
        return this.acilanUygulamalar;
    }

    static interface User32 extends StdCallLibrary {

        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

        interface WNDENUMPROC extends StdCallCallback {

            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

        int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

        Pointer GetWindow(Pointer hWnd, int uCmd);
    }

    public static List<String> getAllWindowNames() {
        final List<String> windowNames = new ArrayList<String>();
        final User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new User32.WNDENUMPROC() {

            public boolean callback(Pointer hWnd, Pointer arg) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText).trim();
                if(!wText.isEmpty()) {
                    windowNames.add(wText);
                }
                return true;
            }
        }, null);

        return windowNames;
    }

    public int rpSkor() {

        return ts.getSkor();
    }

    public RunningProcesses(String blockedApps, boolean internetiKapat) {
        this.internetiKapat = internetiKapat;
        blockedAppsList = Arrays.asList(blockedApps.split(","));
        for(String element : blockedAppsList) {
            rw.addText("These programs are set to be blocked : " + element, 1);
        }

        try {
            Process k = Runtime.getRuntime().exec(
                    "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Advanced /v HideFileExt /t REG_DWORD /d 0 /f");
        }
        catch(IOException ex) {
            Logger.getLogger(RunningProcesses.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        ts = new TrustScore();
    }

    public static void main(String[] args) {

    }

    public void run() {
        List<String> chromeList = new ArrayList<String>();
        List<String> firefoxList = new ArrayList<String>();
        List<String> explorerList = new ArrayList<String>();
        List<String> edgeList = new ArrayList<String>();

        List<String> winNameList = getAllWindowNames();
        for(String winName : winNameList) {

            if( !((winName.contains("Default IME")) || (winName.contains("MSCTFIME UI"))) ) {
                rw.addText(winName, 2);

            }

            if(winName.contains("Google Chrome")) {
                if(chromeList.size() == 0) {
                    chromeList.add(winName);
                }
                else if(!(chromeList.get(0).equals(winName))) {
                    chromeList.add(winName);
                }

            }
            if(winName.contains("Mozilla Firefox")) {

                firefoxList.add(winName);
            }
            if(winName.contains("Internet Explorer")) {

                if(explorerList.size() == 0) {
                    explorerList.add(winName);
                }
                else if(!(explorerList.get(0).equals(winName))) {
                    explorerList.add(winName);
                }
            }
            if(winName.contains("Microsoft Edge")) {

                if(edgeList.size() == 0) {
                    edgeList.add(winName);
                }
                else if(!(edgeList.get(0).equals(winName))) {
                    edgeList.add(winName);
                }
            }

        }

        String eskiChromeSekme = "";
        String eskiFirefoxSekme = "";

        String yeniChromeSekme = "";
        String yeniFirefoxSekme = "";

        String eskiExplorerSekme = "";
        String yeniExplorerSekme = "";

        String eskiEdgeSekme = "";
        String yeniEdgeSekme = "";

        if(!blockedAppsList.isEmpty()) {
            for(String element : blockedAppsList) {

                try {
                    String line;
                    Process p = Runtime.getRuntime().exec(
                            System.getenv("windir") + "\\system32\\" + "tasklist.exe /fo csv /nh");
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(
                                    p.getInputStream()));
                    while((line = input.readLine()) != null) {
                        if(line.toLowerCase().contains(element)) {
                            System.out.println(element + "closing..");
                            rw.addText(line + " closed.", 3);
                            String[] dizi = line.split(",");
                            String temp = dizi[1].substring(1,
                                    dizi[1].length() - 1);
                            int pid = Integer.parseInt(temp);

                            Process k = Runtime.getRuntime().exec(
                                    "taskkill /pid " + pid);

                            ts.skorArttir(2);

                        }
                    }
                    input.close();
                }
                catch(Exception err) {
                    err.printStackTrace();
                }

            }

        }
        if(chromeList.size() > 1 && internetiKapat) {

            ts.skorArttir(2);
            rw.addText("Google Chrome 1'den fazla sekme acik.", 4);
            System.out.println(
                    "1'den fazla Google Chrome penceresi acik. Sadece 1 pencere acik olacak sekilde digerlerini kapatin.");
            /*
             * JOptionPane.showMessageDialog(null,
             * "1'den fazla chrome penceresi acik. Sadece 1 pencere acik olacak
             * sekilde digerlerini kapatin.",
             * "Hata", JOptionPane.ERROR_MESSAGE);
             *
             */

        }
        else if(chromeList.size() == 1) {
            eskiChromeSekme = chromeList.get(0);
        }

        if(firefoxList.size() > 1 && internetiKapat) {

            ts.skorArttir(2);
            rw.addText("Mozilla Firefox 1'den fazla sekme acik.", 4);
            System.out.println(
                    "1'den fazla firefox penceresi acik. Sadece 1 pencere acik olacak sekilde digerlerini kapatin.");
            /*
             * JOptionPane.showMessageDialog(null,
             * "1'den fazla Mozilla Firefox penceresi acik. Sadece 1 pencere
             * acik olacak sekilde digerlerini kapatin.",
             * "Hata", JOptionPane.ERROR_MESSAGE);
             *
             */
        }
        else if(firefoxList.size() == 1) {
            eskiFirefoxSekme = firefoxList.get(0);
        }

        if(explorerList.size() > 1 && internetiKapat) {

            ts.skorArttir(2);
            rw.addText("Internet Explorer 1'den fazla sekme acik.", 4);
            System.out.println(
                    "1'den fazla explorer penceresi acik. Sadece 1 pencere acik olacak sekilde digerlerini kapatin.");
            /*
             * JOptionPane.showMessageDialog(null,
             * "1'den fazla Internet Explorer penceresi acik. Sadece 1 pencere
             * acik olacak sekilde digerlerini kapatin.",
             * "Hata", JOptionPane.ERROR_MESSAGE);
             *
             *
             * for(String expName : explorerList) {
             * System.out.println(expName);
             * }
             */
        }
        else if(explorerList.size() == 1) {
            eskiExplorerSekme = explorerList.get(0);
        }

        if(edgeList.size() > 1 && internetiKapat) {

            ts.skorArttir(2);
            rw.addText("Microsoft Edge 1'den fazla sekme acik.", 4);

            System.out.println(
                    "1'den fazla edge penceresi acik. Sadece 1 pencere acik olacak sekilde digerlerini kapatin.");
            /*
             * JOptionPane.showMessageDialog(null,
             * "1'den fazla Microsoft Edge penceresi acik. Sadece 1 pencere acik
             * olacak sekilde digerlerini kapatin.",
             * "Hata", JOptionPane.ERROR_MESSAGE);
             *
             *
             * for(String edgeName : edgeList) {
             * System.out.println(edgeName);
             * }
             */
        }
        else if(edgeList.size() == 1) {
            eskiEdgeSekme = edgeList.get(0);
        }

        while(true) {
            List<String> winNameList2 = getAllWindowNames();
            Set foo = new HashSet(winNameList);
            Set foo2 = new HashSet(winNameList2);

            //List<String> fark = Sets.symmetricDifference(foo, foo2);
            Set fark = Sets.symmetricDifference(foo, foo2);

            //System.out.println(fark.toString());
            if(foo.size() > foo2.size()) {
                rw.addText("" + fark, 3);
                //System.out.println("" + fark);
                kapatilanUygulamalar.add(fark.toString());

            }
            else if(foo2.size() > foo.size()) {
                rw.addText("" + fark, 6);
                //rw.addText("" + fark, 2);
                //System.out.println("" + fark);
                acilanUygulamalar.add(fark.toString());
            }

            //winNameList = getAllWindowNames();
            for(String winName : winNameList) {
                if(winName.contains("Google Chrome")) {
                    yeniChromeSekme = winName;

                }
                if(winName.contains("Mozilla Firefox")) {
                    yeniFirefoxSekme = winName;

                }
                if(winName.contains("Internet Explorer")) {
                    yeniExplorerSekme = winName;

                }
                if(winName.contains("Microsoft Edge")) {
                    yeniEdgeSekme = winName;

                }

                /*
                 * if(winName.contains("pdf")){
                 * JOptionPane.showMessageDialog(null,
                 * "PDF dosyasi acik !", "Hata", JOptionPane.ERROR_MESSAGE);
                 * System.out.println("Pdf dosyasi acik ! " + winName);
                 * }
                 *
                 * if(winName.contains("Office")){
                 * JOptionPane.showMessageDialog(null,
                 * "Office dosyasi acik !", "Hata", JOptionPane.ERROR_MESSAGE);
                 * System.out.println("Office dosyasi acik ! " + winName);
                 * }
                 *
                 * if(winName.contains("Word")){
                 * JOptionPane.showMessageDialog(null,
                 * "Word dosyasi acik !", "Hata", JOptionPane.ERROR_MESSAGE);
                 * System.out.println("Word dosyasi acik ! " + winName);
                 * }
                 * if(winName.contains("Powerpoint")){
                 * JOptionPane.showMessageDialog(null,
                 * "Powerpoint dosyasi acik !", "Hata",
                 * JOptionPane.ERROR_MESSAGE);
                 * System.out.println("Powerpoint dosyasi acik ! " + winName);
                 * }
                 * if(winName.contains("jpg") || winName.contains("png")){
                 * JOptionPane.showMessageDialog(null,
                 * "Fotograf dosyasi acik !", "Hata",
                 * JOptionPane.ERROR_MESSAGE);
                 * System.out.println("Fotoğraflar dosyasi acik ! " + winName);
                 * }
                 */
                for(String element : blockedAppsList) {

                    if(winName.toLowerCase().contains(element.toLowerCase())) {
                        //JOptionPane.showMessageDialog(null, 
                        //element +" dosyasi acik !", "Hata", JOptionPane.ERROR_MESSAGE);
                        rw.addText("Blocked app "
                                + element + " opened. File Name:  " + winName, 1);

                        if(!blockedAppsList.isEmpty()) {
                            boolean temp = false;

                            temp = closeApp(element.toLowerCase());

                            if(!temp) {
                                if(!winName.toLowerCase().contains("pdf")) {
                                    killByExtension(element, winName);
                                }
                            }
                        }
                        rw.addText("Blocked app  "
                                + winName + " closed.", 1);
                    }

                }

            }

            //
            if(internetiKapat) {

                if(!(eskiChromeSekme.equals(yeniChromeSekme))) {

                    /*
                     * JOptionPane.showMessageDialog(null,
                     * "Google Chrome sekmesi degistirildi", "Hata",
                     * JOptionPane.ERROR_MESSAGE);
                     *
                     *
                     */
                    rw.addText("Google Chrome sekmesi degistirildi !!!", 4);
                    rw.addText("Eski sekme: " + eskiChromeSekme, 4);
                    rw.addText("Yeni sekme: " + yeniChromeSekme, 4);
                    ts.skorArttir(2);

                    eskiChromeSekme = yeniChromeSekme;

                }
                if(!(eskiFirefoxSekme.equals(yeniFirefoxSekme))) {
                    /*
                     * JOptionPane.showMessageDialog(null,
                     * "Mozilla Firefox sekmesi degistirildi", "Hata",
                     * JOptionPane.ERROR_MESSAGE);
                     *
                     */
                    rw.addText("Mozilla Firefox sekmesi degistirildi !!!", 4);
                    rw.addText("Eski sekme: " + eskiFirefoxSekme, 4);
                    rw.addText("Yeni sekme: " + yeniFirefoxSekme, 4);

                    ts.skorArttir(2);

                    eskiFirefoxSekme = yeniFirefoxSekme;

                }
                if(!(eskiExplorerSekme.equals(yeniExplorerSekme))) {
                    /*
                     * JOptionPane.showMessageDialog(null,
                     * "Internet Explorer sekmesi degistirildi", "Hata",
                     * JOptionPane.ERROR_MESSAGE);
                     */
                    rw.addText("Explorer sekmesi degistirildi !!!", 4);
                    rw.addText("Eski sekme: " + eskiExplorerSekme, 4);
                    rw.addText("Yeni sekme: " + yeniExplorerSekme, 4);

                    ts.skorArttir(2);

                    eskiExplorerSekme = yeniExplorerSekme;

                }
                if(!(eskiEdgeSekme.equals(yeniEdgeSekme))) {
                    /*
                     * JOptionPane.showMessageDialog(null,
                     * "Microsoft Edge sekmesi degistirildi", "Hata",
                     * JOptionPane.ERROR_MESSAGE);
                     *
                     */
                    rw.addText("Microsoft Edge sekmesi degistirildi !!!", 4);
                    rw.addText("Eski sekme: " + eskiEdgeSekme, 4);
                    rw.addText("Yeni sekme: " + yeniEdgeSekme, 4);

                    ts.skorArttir(2);

                    eskiEdgeSekme = yeniEdgeSekme;

                }
            }

            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                //throw new RuntimeException(e);
            }

            winNameList = winNameList2;
        }
    }

    private boolean closeApp(String taskName) {

        boolean kapatildiMi = false;

        try {
            String line;
            Process p = Runtime.getRuntime().exec(
                    System.getenv("windir") + "\\system32\\" + "tasklist.exe /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            while((line = input.readLine()) != null) {
                if(line.toLowerCase().contains(taskName)) {
                    //rw.addText(taskName + "Kapatiliyor.", 3);
                    //System.out.println(taskName + "Kapatiliyor.");
                    String[] dizi = line.split(",");
                    String temp = dizi[1].substring(1, dizi[1].length() - 1);
                    int pid = Integer.parseInt(temp);
                    Process k = Runtime.getRuntime().exec("taskkill /pid " + pid);
                    rw.addText(taskName + " kapatildi.", 3);
                    ts.skorArttir(2);

                    kapatildiMi = true;
                }
                if(taskName.equalsIgnoreCase("pdf") && line.toLowerCase().contains(
                        "AcroRd32.exe".toLowerCase())) {
                    //rw.addText(taskName + "Kapatiliyor.", 3);
                    //System.out.println(taskName + "Kapatiliyor.");
                    String[] dizi = line.split(",");
                    String temp = dizi[1].substring(1, dizi[1].length() - 1);
                    int pid = Integer.parseInt(temp);
                    Process k = Runtime.getRuntime().exec("taskkill /pid " + pid);
                    rw.addText(taskName + " kapatildi.", 3);
                    ts.skorArttir(2);
                    kapatildiMi = true;

                }
                if(line.toLowerCase().contains("POWERPNT.EXE".toLowerCase())) {
                    //rw.addText(taskName + "Kapatiliyor.", 3);
                    //System.out.println(taskName + "Kapatiliyor.");
                    String[] dizi = line.split(",");
                    String temp = dizi[1].substring(1, dizi[1].length() - 1);
                    int pid = Integer.parseInt(temp);
                    Process k = Runtime.getRuntime().exec("taskkill /pid " + pid);
                    rw.addText(taskName + " kapatildi.", 3);
                    ts.skorArttir(2);

                    kapatildiMi = true;

                }
                if(line.toLowerCase().contains("edge")) {
                    String[] dizi = line.split(",");
                    String temp = dizi[1].substring(1, dizi[1].length() - 1);
                    int pid = Integer.parseInt(temp);
                    Process k = Runtime.getRuntime().exec(
                            "taskkill /F /PID " + pid);

                    rw.addText(taskName + " kapatildi.", 3);
                    ts.skorArttir(2);

                    kapatildiMi = true;

                }
                if(line.toLowerCase().contains("notepad")) {
                    String[] dizi = line.split(",");
                    String temp = dizi[1].substring(1, dizi[1].length() - 1);
                    int pid = Integer.parseInt(temp);
                    Process k = Runtime.getRuntime().exec(
                            "taskkill /F /PID " + pid);

                    rw.addText(taskName + " kapatildi.", 3);
                    ts.skorArttir(2);

                    kapatildiMi = true;

                }

            }
            input.close();
        }
        catch(Exception err) {
            err.printStackTrace();
        }
        return kapatildiMi;
    }

    private void killByExtension(String extension, String winName) {

        try {
            ArrayList<String> cmds = new ArrayList<String>();

            cmds.add("wmic");
            cmds.add("process");
            cmds.add("get");
            cmds.add("commandline,processid");

            ProcessBuilder pb = new ProcessBuilder(cmds);

            Process p = pb.start();

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String line;
            int pid = 0;

            while((line = rd.readLine()) != null) {
                if(line.toLowerCase().contains(extension) && !line.contains(
                        "Tilki")
                        && !line.contains("RunningProcesses") && !line.contains(
                        "reportForTeacher")) {

                    String[] split = line.split(" ");
                    pid = Integer.parseInt(split[split.length - 1]);
                }
                else {
                }
            }
            /*
             * cmds = new ArrayList<String>();
             *
             * cmds.add("taskkill");
             * cmds.add("/T");
             * cmds.add("/F");
             * cmds.add("/PID");
             * cmds.add("" + pid);
             *
             * pb = new ProcessBuilder(cmds);
             * pb.start();
             */

            String cmd = "taskkill /F /PID " + pid;
            Runtime.getRuntime().exec(cmd);
            ts.skorArttir(2);

            rw.addText(
                    "File :" + winName + " with " + extension + " extension closed",
                    3);

        }
        catch(IOException ex) {
            Logger.getLogger(RunningProcesses.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }

}
