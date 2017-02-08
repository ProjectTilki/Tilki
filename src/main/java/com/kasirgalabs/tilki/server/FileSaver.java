package com.kasirgalabs.tilki.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSaver extends DefaultService {

    public FileSaver(Socket socket) throws IOException {
        super(socket);
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
    @Override
    public void serve() throws IOException, NoSuchAlgorithmException {
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
            fileOut = new FileOutputStream(incomingFile); // Creates a file to be filled.
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
}
