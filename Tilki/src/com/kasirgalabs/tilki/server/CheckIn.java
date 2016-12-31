package com.kasirgalabs.tilki.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckIn extends DefaultService {

    public CheckIn(Socket socket) throws IOException {
        super(socket);
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
    @Override
    public void serve() throws IOException {
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
            if(file.exists()) { // if log file is exists, student trying to reconnect else check in.
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
}
