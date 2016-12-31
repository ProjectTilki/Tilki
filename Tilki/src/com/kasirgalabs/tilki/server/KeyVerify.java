package com.kasirgalabs.tilki.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyVerify implements Service {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    /**
     * Constructor for initializing instance variables.
     *
     * @param socket The incoming socket for receiving and sending data.
     *
     * @throws java.io.IOException If an IO error occurs on socket's streams.
     */
    public KeyVerify(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
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
    @Override
    public void serve() throws IOException {
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
}
