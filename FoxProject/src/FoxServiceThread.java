import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

public class FoxServiceThread implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader br_in;
    public FoxServiceThread(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(socket.getOutputStream());
            br_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        if(data.equals("Enrollment.")) {
            try {
                enrollmentManager();
            } catch (IOException ex) {
                Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(data.equals("Sending file.")) {
            try {
                fileManager();
            } catch (IOException ex) {
                Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(data.equals("List exams.")) {
            try {
                examListManager();
            } catch (IOException ex) {
                Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, "An error has occured.");
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enrollmentManager() throws IOException {
        String name = br_in.readLine();
        String surname = br_in.readLine();
        String id = br_in.readLine();
        String exam = br_in.readLine();
        String instructorKey = br_in.readLine();
        File examFolder = new File(exam);
        File examKeyFile = new File(examFolder, "exam_key.txt");
        File logFile = new File(examFolder, id + "_logfile.txt");
        if(!examKeyFile.exists()) {
            out.println("0");
            return;
        }
        BufferedReader fileIn = new BufferedReader(new FileReader(examKeyFile));
        String lines;
        PrintWriter fileOut = null;
        while((lines = fileIn.readLine()) != null) {
            if(lines.trim().equals(instructorKey)) {
                if(logFile.exists()) {
                    fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                    fileOut.print(id + " | ");
                    fileOut.print(name + " ");
                    fileOut.print(surname + " | ");
                    fileOut.print(instructorKey + " --> ");
                    fileOut.println("Reconnected.");
                    out.println("1");
                }
                else {
                    fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                    fileOut.print(id + " | ");
                    fileOut.print(name + " ");
                    fileOut.print(surname + " | ");
                    fileOut.print(instructorKey + " --> ");
                    fileOut.println("Enrolled.");
                    out.println("2");
                }
                break;
            }
            else {
                fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                fileOut.print(id + " | ");
                fileOut.print(name + " ");
                fileOut.print(surname + " | ");
                fileOut.print(instructorKey + " --> ");
                fileOut.println("Instructor key is not accepted.");
                out.println("3");
            }
        }
        fileIn.close();
        fileOut.close();
        out.flush();
    }

    private void fileManager() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        String fileName = "0_" + br_in.readLine();
        String id = br_in.readLine();
        String exam = br_in.readLine();
        File examFile = new File(exam);
        File incomingFile = new File(examFile, fileName);
        FileOutputStream fileOut = new FileOutputStream(incomingFile);
        int byteCount;
        byte[] data = new byte[1024];
        InputStream os_in = socket.getInputStream();
        while((byteCount = os_in.read(data)) > 0)
            fileOut.write(data, 0, byteCount);
        fileOut.flush();
        fileOut.close();
        FileInputStream fileIn = new FileInputStream(incomingFile);
        byte[] fileData = new byte[1024];
        MessageDigest md = MessageDigest.getInstance("MD5");
        while((byteCount = fileIn.read(fileData)) > 0)
            md.update(fileData, 0, byteCount);
        byte[] rawChecksum = md.digest();
        StringBuilder md5hex = new StringBuilder();
        for(int i = 0; i < rawChecksum.length; i++)
            md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100, 16).substring(1));
        out.println(md5hex.toString());
        out.flush();
        fileIn.close();
    }

    private void examListManager() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        if(!new File("exam_list.txt").exists()) {
            oos.writeObject(null);
            oos.flush();
            return;
        }
        BufferedReader fileIn = new BufferedReader(new FileReader("exam_list.txt"));
        ArrayList<Exam> examList = new ArrayList<>();
        String exam;
        while((exam = fileIn.readLine()) != null) {
            if(!new File(exam, "exam_description.txt").exists()) {
                examList.add(new Exam(exam, null));
                continue;
            }
            BufferedReader description = new BufferedReader(new FileReader(new File(exam, "exam_description.txt")));
            String examDescription = "";
            String s;
            while((s = description.readLine()) != null)
                examDescription += s;
            examList.add(new Exam(exam, examDescription));
        }
        fileIn.close();
        Exam[] exams = new Exam[examList.size()];
        for(int i = 0; i < examList.size(); i++)
            exams[i] = examList.get(i);
        oos.writeObject(exams);
        oos.flush();
        socket.close();
    }
}
