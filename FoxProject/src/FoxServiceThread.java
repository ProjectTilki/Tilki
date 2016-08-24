import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
        else
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, "An error has occured.");
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enrollmentManager() {
        String name = null;
        String surname = null;
        String id = null;
        String exam = null;
        String instructorKey = null;
        try {
            name = br_in.readLine();
            surname = br_in.readLine();
            id = br_in.readLine();
            exam = br_in.readLine();
            instructorKey = br_in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        File examFolder = new File(exam);
        File examKeyFile = new File(examFolder, "exam_key.txt");
        File logFile = new File(examFolder, id + "_logfile.txt");
        BufferedReader fileIn = null;
        try {
            if(!examKeyFile.exists()) {
                out.println("3");
                return;
            }
            fileIn = new BufferedReader(new FileReader(examKeyFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            out.println("3");
            try {
                if(fileIn != null)
                    fileIn.close();
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return; 
        }
        String lines;
        PrintWriter fileOut = null;
        try {
            while((lines = fileIn.readLine()) != null) {
                if(lines.trim().equals(instructorKey)) {
                    if(logFile.exists()) {
                        fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                        fileOut.print(id + " | ");
                        fileOut.print(name + " ");
                        fileOut.print(surname + " | ");
                        fileOut.print(instructorKey + " --> ");
                        fileOut.println("Reconnected.");
                        out.println("2");
                    }
                    else {
                        fileOut = new PrintWriter(new FileOutputStream(logFile, true));
                        fileOut.print(id + " | ");
                        fileOut.print(name + " ");
                        fileOut.print(surname + " | ");
                        fileOut.print(instructorKey + " --> ");
                        fileOut.println("Enrolled.");
                        out.println("0");
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if(fileOut != null)
                fileIn.close();
            if(fileOut != null)
                fileOut.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        ArrayList<Exam> examList = new ArrayList<Exam>();
        try {
            String exam;
            while((exam = fileIn.readLine()) != null) {
                BufferedReader description = new BufferedReader(new FileReader(new File(exam, "exam_description.txt")));
                String examDescription = "";
                String s;
                while((s = description.readLine()) != null)
                    examDescription += s;
                examList.add(new Exam(exam, examDescription));
            }
        } catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fileIn.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        Exam[] exams = new Exam[examList.size()];
        for(int i = 0; i < examList.size(); i++)
            exams[i] = examList.get(i);
        try {
            oos.writeObject(exams);
            oos.flush();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
