package kasirgalabs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoxProject server side utilities. This class consists of methods that operate
 * on client requests. Main purpose of this class log the exam attendance
 * request and save incoming files.
 */
public class FoxServiceThread implements Callable<Integer> {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     *
     * @throws java.io.IOException If an IO error occurs on socket's streams.
     */
    public FoxServiceThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Thread starts execution on this method.
     * <p>
     * Reads data from an incoming connection and decides what to do. This
     * method returns if and only if operation is successful.
     *
     * @return 1 if operation is successful.
     *
     * @throws Exception if unable to compute the result.
     */
    @Override
    public Integer call() throws Exception {
        try {
            String data = in.readUTF(); // Read the requested operation.
            if(data.equals("Check in")) {
                checkInManager();
            }
            else if(data.equals("Key verify")) {
                keyVerifyManager();
            }
            else if(data.equals("Sending file")) {
                fileManager();
            }
            else if(data.equals("List exams")) {
                examListManager();
            }
            else if(data.equals("Sending error logs")) {
                saveErrorLogs();
            }
        }
        catch(IOException e) {
            Logger.getLogger(FoxServer.class.getName()).log(Level.SEVERE,
                    "Error during session.", e);
        }
        catch(NoSuchAlgorithmException e) {
            Logger.getLogger(FoxServer.class.getName()).log(Level.SEVERE,
                    "Error during session.", e);
        }
        finally {
            socket.close();
        }
        return 1;
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
        PrintWriter logFile = null;
        try {
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
            // if log file is exists, student trying to reconnect else check in.
            if(file.exists()) {
                isCheckedIn = true;
            }

            logFile = new PrintWriter(new FileOutputStream(file,
                    true));
            logFile.print(id + " | ");
            logFile.print(name + " ");
            logFile.print(surname + " | ");
            if(isCheckedIn) {
                logFile.print("--> Reconnected. | ");
                out.writeUTF("1");
            }
            else {
                logFile.print("--> Check in. | ");
                out.writeUTF("2");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            logFile.println(dateFormat.format(date));

            logFile.close();
        }
        finally {
            if(logFile != null) {
                logFile.close();
            }
        }
    }

    /**
     * Verifies a student check in if the instructor key matches with any of the
     * keys found in "exam_key.txt". Receives data from a
     * {@link java.net.Socket} object.
     * <p>
     * Method searches exam folder to find "exam_key.txt". Key file may has more
     * than one key.
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

        // Create File references.
        File examFileObject = new File(exam);
        File examKeyFileObject = new File(examFileObject, "exam_key.txt");
        // Check if exam folder or "exam_key.txt" file is missing.
        if(!examFileObject.exists() || !examKeyFileObject.exists()) {
            out.writeUTF("0");
            out.flush();
            return;
        }

        BufferedReader examKeyFile = new BufferedReader(new FileReader(
                examKeyFileObject));

        File studentFile = new File(examFileObject, id);
        if(!studentFile.exists() || !studentFile.isDirectory()) {
            if(!studentFile.mkdir()) {
                return;
            }
        }

        File logFileObject = new File(studentFile, id + "_logfile.txt");

        PrintWriter logFile = new PrintWriter(
                new FileOutputStream(logFileObject, true));

        logFile.print(id + " | ");
        logFile.print(name + " ");
        logFile.print(surname + " | ");
        logFile.print(instructorKey + " --> ");

        String lines;
        boolean isAccepted = false;

        // Match keys.
        while((lines = examKeyFile.readLine()) != null && !isAccepted) {
            if(lines.equals(instructorKey)) { // Case sensitive key matching.
                logFile.print("Instructor key is accepted. | ");
                out.writeUTF("1");
                isAccepted = true;
            }
        }

        if(!isAccepted) { // Key is not matched.
            logFile.print("Instructor key is not accepted. | ");
            out.writeUTF("2");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        logFile.println(dateFormat.format(date));

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
        PrintWriter logFile = null;
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        try {
            String fileName = in.readUTF();
            String id = in.readUTF();
            String exam = in.readUTF();

            File examFileObject = new File(exam);
            File studentFile = new File(examFileObject, id);
            if(!examFileObject.exists()) {
                out.writeUTF("Exam file is missing.");
                out.flush();
                return;
            }
            else {
                out.writeUTF("Exam file is found.");
                out.flush();
            }

            File temp = new File(studentFile, fileName);
            if(temp.exists()) { // File exists, try finding new file name.
                for(int i = 0; i < 100; i++) {
                    temp = new File(studentFile, i + "_" + fileName);
                    if(!temp.exists()) {
                        fileName = i + "_" + fileName;
                        break;
                    }
                }
            }

            MessageDigest md = MessageDigest.getInstance("MD5");
            int byteCount;
            byte[] data = new byte[1024];
            InputStream os_in = socket.getInputStream();
            File incomingFile = new File(studentFile, fileName);
            // Creates a file to be filled.
            fileOut = new FileOutputStream(incomingFile);
            // Read file data from the socket and write it to a created file.
            while((byteCount = os_in.read(data)) > 0) {
                fileOut.write(data, 0, byteCount);
                md.update(data, 0, byteCount);
            }

            logFile = new PrintWriter(new FileOutputStream(new File(studentFile,
                    id + "_logfile.txt"), true));

            logFile.print(id + " | ");
            logFile.print("File is received. | ");
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            logFile.println(dateFormat.format(date));

            byte[] rawChecksum = md.digest();
            StringBuilder md5hex = new StringBuilder();
            for(int i = 0; i < rawChecksum.length; i++) {
                md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100,
                        16).substring(1));
            }
            out.writeUTF(md5hex.toString()); // Send MD5 checksum to the client.
            out.flush();
        }
        finally {
            if(logFile != null) {
                logFile.close();
            }
            if(fileIn != null) {
                fileIn.close();
            }
            if(fileOut != null) {
                fileOut.close();
            }
        }
    }

    /**
     * This method searches for "exam_list.txt" in current working directory to
     * find exam folders. For each line in "exam_list.txt" represents an exam
     * folder. If exam name exists in "exam_list.txt" but current working
     * directory does not have a folder with exam name, exam won't be listed as
     * available.
     * <p>
     * On request will send available exams to the client over socket along with
     * their descriptions if exists.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void examListManager() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
                socket.getOutputStream());
        if(!new File("exam_list.txt").exists()) { // No exams are available.
            oos.writeObject(null);
            oos.flush();
            return;
        }

        BufferedReader fileIn = new BufferedReader(new FileReader(
                "exam_list.txt"));
        // Array list of Exam object contains available exams.
        ArrayList<Exam> examList = new ArrayList<Exam>();
        String exam;
        String examDescription = "";
        String temp;
        Boolean examStatus;
        while((exam = fileIn.readLine()) != null) { // Read exam list.
            if(new File(exam).exists()) {
                // Look for the descripton.
                if(!new File(exam, "exam_description.txt").exists()) {
                    examDescription = null;
                }
                else {
                		BufferedReader description = new BufferedReader(new FileReader(
                            new File(exam, "exam_description.txt")));
                		 boolean firstLine = true;
                         // Description is available.
                         while((temp = description.readLine()) != null) {
                             if(firstLine) {
                                 examDescription += temp;
                                 firstLine = false;
                                 continue;
                             }
                             examDescription += "\n" + temp;
                             description.close();
                         }
                }
                
                if(!new File(exam, "exam_status.txt").exists()) {
                		examStatus = false;
                }
                else {
                		BufferedReader examStatus_br = new BufferedReader(new FileReader(
                            new File(exam, "exam_status.txt")));
                		temp = examStatus_br.readLine();
                		
                		if(temp.equals("true")) {
                			examStatus = true;
                		}
                		else
                			examStatus = false;
                		
                		examStatus_br.close();

                }
                examList.add(new Exam(exam, examDescription,examStatus));
            }
        }
        fileIn.close();

        Exam[] exams = new Exam[examList.size()];
        // Convert array list to array.
        for(int i = 0; i < examList.size(); i++) {
            exams[i] = examList.get(i);
        }
        oos.writeObject(exams); // Send exam list to the client.
        oos.flush();
    }

    private synchronized void saveErrorLogs() throws IOException {
        BufferedWriter fileOut = null;
        try {
            int lineCount = in.readInt();
            File f = new File("clientErrors.log");
            fileOut = new BufferedWriter(new FileWriter(f, true));
            String line;
            while(lineCount > 0) {
                line = in.readUTF();
                fileOut.write(line + "\n");
                lineCount--;
            }
        }
        finally {
            if(fileOut != null) {
                fileOut.close();
            }
        }
    }
}
