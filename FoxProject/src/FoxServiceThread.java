import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoxServiceThread implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    public FoxServiceThread(Socket socket) {
        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String data = in.readUTF();
            if(data.equals("Enrollment.")) {
                enrollmentManager();
            }
            else if(data.equals(("Key verify."))) {
                authorizationManager();
            }
            else if(data.equals("Sending file.")) {
                    fileManager();
            }
            else if(data.equals("List exams.")) {

                    examListManager();
            }
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enrollmentManager() throws IOException {
        String name = in.readUTF();
        String surname = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();
        File examFolder = new File(exam);
        File logFile = new File(examFolder, id + "_logfile.txt");
        PrintWriter fileOut;
        if(logFile.exists()) {
            fileOut = new PrintWriter(new FileOutputStream(logFile, true));
            fileOut.print(id + " | ");
            fileOut.print(name + " ");
            fileOut.print(surname + " | ");
            fileOut.println("--> Reconnected.");
            out.writeUTF("0");
        }
        else {
            fileOut = new PrintWriter(new FileOutputStream(logFile, true));
            fileOut.print(id + " | ");
            fileOut.print(name + " ");
            fileOut.print(surname + " | ");
            fileOut.println("--> Check In.");
            out.writeUTF("1");
        }
        fileOut.close();
        out.flush();
    }
    
    private void authorizationManager() throws IOException {
        String name = in.readUTF();
        String surname = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();
        String instructorKey = in.readUTF();
        File examFolder = new File(exam);
        File examKeyFile = new File(examFolder, "exam_key.txt");
        File logFile = new File(examFolder, id + "_logfile.txt");
        if(!examKeyFile.exists()) {
            out.writeUTF("0");
            return;
        }
        BufferedReader fileIn = new BufferedReader(new FileReader(examKeyFile));
        String lines;
        PrintWriter fileOut = null;
        while((lines = fileIn.readLine()) != null) {
            if(lines.trim().equals(instructorKey)) {
                fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                fileOut.print(id + " | ");
                fileOut.print(name + " ");
                fileOut.print(surname + " | ");
                fileOut.print(instructorKey + " --> ");
                fileOut.println("Instructor key is accepted.");
                out.writeUTF("1");
            }
            else {
                fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                fileOut.print(id + " | ");
                fileOut.print(name + " ");
                fileOut.print(surname + " | ");
                fileOut.print(instructorKey + " --> ");
                fileOut.println("Instructor key is not accepted.");
                out.writeUTF("2");
            }
        }
        fileIn.close();
        if(fileOut != null)
            fileOut.close();
        out.flush();
    }

    /**
     * Saves a file from an incoming socket connection.
     * The file name is received from socket. If the file exists, adds some text to file name
     * as prefix and saves it.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException 
     */
    private void fileManager() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String fileName = dis.readUTF();
        String id = dis.readUTF();
        String exam = dis.readUTF();
        File examFile = new File(exam);
        
        File file = new File(examFile, fileName);
        if(file.exists()) {
            for(int i = 0; i < 100; i++) {
                file = new File(examFile, i + "_" + fileName);
                if(!file.exists()) {
                    fileName = i + "_" + fileName;
                    break;
                }
            }
        }
        
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
        out.writeUTF(md5hex.toString());
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
