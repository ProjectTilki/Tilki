import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoxServiceThread implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader br_in;
    private InputStream os_in;

    public FoxServiceThread(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream(), true);
            br_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os_in = socket.getInputStream();
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String data = null;
        try {
            data = br_in.readLine();
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(data.equals("Enrollment."))
            enrollmentManager();
        else if(data.equals("Sending file."))
            fileManager();
        else if(data.equals("List exams."))
            examListManager();
        else if(data.equals("Exam description."))
            examDescriptionManager();
        else
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, "An error has occured.");
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void enrollmentManager() {

    }

    private void fileManager() {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("not_completed_yet.txt");
        } catch(FileNotFoundException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        int byteCount = 0;
        byte[] data = new byte[1024];
        try {
            while((byteCount = os_in.read(data)) > 0)
                fileOut.write(data, 0, byteCount);
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream("not_completed_yet.txt");
        } catch(FileNotFoundException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] fileData = new byte[1024];
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while((byteCount = fileIn.read(fileData)) > 0)
                md.update(fileData, 0, byteCount);
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] rawChecksum = md.digest();

        StringBuilder md5hex = new StringBuilder();
        for(int i = 0; i < rawChecksum.length; i++)
            md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100, 16).substring(1));
        out.println(md5hex.toString());

        try {
            fileIn.close();
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void examListManager() {
        BufferedReader fileIn = null;
        try {
            fileIn = new BufferedReader(new FileReader("exam_list.txt"));
        } catch(FileNotFoundException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        String exam;
        try {
            while((exam = fileIn.readLine()) != null)
                out.println(exam);
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fileIn.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void examDescriptionManager() {
        BufferedReader fileIn = null;
        String exam;
        try {
            exam = br_in.readLine();
            fileIn = new BufferedReader(new FileReader("exam_list.txt"));
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        String description;
        try {
            while((description = fileIn.readLine()) != null)
                out.println(description);
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fileIn.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
