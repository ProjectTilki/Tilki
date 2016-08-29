import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

/**
 * FoxProject server side utilities. This class consists of methods that operate
 * on client requests. Main purpose of this class log the exam attendance
 * request and save incoming files.
 */
public class FoxServiceThread implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     */
    public FoxServiceThread(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE,
                                                                   null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String data = in.readUTF(); // Read the requested operation.
            if(data.equals("Check in."))
                checkInManager();
            else if(data.equals(("Key verify.")))
                keyVerifyManager();
            else if(data.equals("Sending file."))
                fileManager();
            else if(data.equals("List exams."))
                examListManager();
            socket.close();
        }catch(IOException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE,
                                                                   null, ex);
        }catch(NoSuchAlgorithmException ex) {
            Logger.getLogger(FoxServiceThread.class.getName()).log(Level.SEVERE,
                                                                   null, ex);
        }
    }

    /**
     * Checks in or reconnects a student to an exam. Student informations will
     * be logged for further use. Uses {@link java.net.Socket} object to receive
     * data.
     * <p>
     * This method will create a log file if and only if exam folder exists and
     * log file does not exists or write to a existing log file in exam folder.
     * If the log file is exists, write operation will be appended. Every
     * information will be read from socket.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void checkInManager() throws IOException {
        // Read informations from socket.
        String name = in.readUTF();
        String surname = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();

        File examFileObject = new File(exam);
        if(!examFileObject.exists()) { // Exam folder is missing.
            out.writeUTF("0");
            out.flush();
            return;
        }

        File file = new File(examFileObject, id + "_logfile.txt");
        boolean isCheckedIn = false;
        if(file.exists()) // if log file is exists, student trying to reconnect else check in.
            isCheckedIn = true;

        PrintWriter logFile = new PrintWriter(new FileOutputStream(file, true));
        logFile.print(id + " | ");
        logFile.print(name + " ");
        logFile.print(surname + " | ");
        if(isCheckedIn) {
            logFile.println("--> Reconnected.");
            out.writeUTF("1");
        }else {
            logFile.println("--> Check in.");
            out.writeUTF("2");
        }

        logFile.close();
        out.flush();
    }

    /**
     * Verifies a student check in if the instructor key matches with any of the
     * keys found in "exam_key.txt". Receives data from a
     * {@link java.net.Socket} object.
     * <p>
     * Method searches exam folder to find "exam_key.txt". Key file may has more
     * than one key.
     * <p>
     * If the student log file does not exists, this method will create a file
     * if the exam folder exists and contains "exam_key.txt". This behavior will
     * lead to a incorrect log status if used without check in.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void keyVerifyManager() throws IOException {
        // Read informations from socket.
        String name = in.readUTF();
        String surname = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();
        String instructorKey = in.readUTF();

        // Create objects to read and write to a file.
        File examFileObject = new File(exam);
        File examKeyFileObject = new File(examFileObject, "exam_key.txt");
        BufferedReader examKeyFile = new BufferedReader(new FileReader(
                examKeyFileObject));

        // Check if exam folder, "exam_key.txt" file are missing.
        if(!examFileObject.exists() || !examKeyFileObject.exists()) {
            out.writeUTF("0");
            examKeyFile.close();
            out.flush();
            return;
        }

        File logFileObject = new File(examFileObject, id + "_logfile.txt");

        // This line will create a log file if one does not exits.
        PrintWriter logFile = new PrintWriter(
                new FileOutputStream(logFileObject, true));

        logFile.print(id + " | ");
        logFile.print(name + " ");
        logFile.print(surname + " | ");
        logFile.print(instructorKey + " --> ");

        String lines;
        boolean isAccepted = false;

        while((lines = examKeyFile.readLine()) != null && !isAccepted) // Match keys.
            if(lines.equals(instructorKey)) { // Case sensitive key matching.
                logFile.println("Instructor key is accepted.");
                out.writeUTF("1");
                isAccepted = true;
            }

        if(!isAccepted) { // Key is not matched.
            logFile.println("Instructor key is not accepted.");
            out.writeUTF("2");
        }

        examKeyFile.close();
        logFile.close();
        out.flush();
    }

    /**
     * Saves a file from an incoming {@link java.net.socket} connection in an
     * exam folder if exam folder exists, calculates and sends it's MD5 checksum
     * over the {@link java.net.socket}.
     * <p>
     * The file name is received from {@link java.net.socket}. If the file
     * exists, adds some text to the file name as prefix and creates a new file
     * without modifying the existing one.
     *
     * @throws IOException              If an I/O error occurs.
     * @throws NoSuchAlgorithmException If no Provider supports a
     *                                  MessageDigestSpi implementation for the
     *                                  specified algorithm.
     */
    private void fileManager() throws IOException, NoSuchAlgorithmException {
        String fileName = in.readUTF();
        String id = in.readUTF();
        String exam = in.readUTF();

        File examFileObject = new File(exam);
        if(!examFileObject.exists()) {
            out.writeUTF("Exam file is missing.");
            out.flush();
            return;
        }else {
            out.writeUTF("Exam file is found.");
            out.flush();
        }

        File temp = new File(examFileObject, fileName);
        if(temp.exists()) // File exists, try finding new file name.
            for(int i = 0; i < 100; i++) {
                temp = new File(examFileObject, i + "_" + fileName);
                if(!temp.exists()) {
                    fileName = i + "_" + fileName;
                    break;
                }
            }

        File incomingFile = new File(examFileObject, fileName);
        FileOutputStream fileOut = new FileOutputStream(incomingFile); // Creates a file to be filled.

        int byteCount;
        byte[] data = new byte[4096];
        InputStream os_in = socket.getInputStream();
        // Read file data from the socket and write it to a created file.
        while((byteCount = os_in.read(data)) > 0)
            fileOut.write(data, 0, byteCount);
        fileOut.close();

        // Open file for reading to calculate it's MD5 checksum.
        FileInputStream fileIn = new FileInputStream(incomingFile);
        byte[] fileData = new byte[4096];
        MessageDigest md = MessageDigest.getInstance("MD5");
        while((byteCount = fileIn.read(fileData)) > 0)
            md.update(fileData, 0, byteCount);
        byte[] rawChecksum = md.digest();
        StringBuilder md5hex = new StringBuilder();
        for(int i = 0; i < rawChecksum.length; i++)
            md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100, 16).
                    substring(1));
        out.writeUTF(md5hex.toString()); // Send MD5 checksum to the client.
        out.flush();
        fileIn.close();
    }

    /**
     * This method searches for current working directory to find exam folders.
     * On request will send available exams to the client over socket along with
     * their descriptions if exists.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void examListManager() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        if(!new File("exam_list.txt").exists()) { // No exams are available.
            oos.writeObject(null);
            oos.flush();
            return;
        }

        BufferedReader fileIn = new BufferedReader(new FileReader(
                "exam_list.txt"));
        ArrayList<Exam> examList = new ArrayList<>(); // Array list of Exam object contains available exams.
        String exam;
        while((exam = fileIn.readLine()) != null) { // Read exam list.
            if(!new File(exam, "exam_description.txt").exists()) { // Look for the descripton.
                examList.add(new Exam(exam, null));
                continue;
            }
            BufferedReader description = new BufferedReader(new FileReader(
                    new File(exam, "exam_description.txt")));
            String examDescription = "";
            String temp;
            boolean firstLine = true;
            while((temp = description.readLine()) != null) // Description is available.
                if(firstLine) {
                    examDescription += temp;
                    firstLine = false;
                }else
                    examDescription += "\n" + temp;
            examList.add(new Exam(exam, examDescription));
        }
        fileIn.close();

        Exam[] exams = new Exam[examList.size()];
        for(int i = 0; i < examList.size(); i++) // Convert array list to array.
            exams[i] = examList.get(i);
        oos.writeObject(exams); // Send exam list to the client.
        oos.flush();
    }
}
