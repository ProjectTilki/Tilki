package com.kasirgalabs.tilki.server;

import com.kasirgalabs.tilki.utils.Exam;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ExamList implements Service {

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
    public ExamList(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
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
    @Override
    public void serve() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        if(!new File("exam_list.txt").exists()) { // No exams are available.
            oos.writeObject(null);
            oos.flush();
            return;
        }

        BufferedReader fileIn = new BufferedReader(new FileReader(
                "exam_list.txt"));
        ArrayList<Exam> examList = new ArrayList<Exam>(); // Array list of Exam object contains available exams.
        String exam;
        while((exam = fileIn.readLine()) != null) { // Read exam list.
            if(new File(exam).exists()) {
                if(!new File(exam, "exam_description.txt").exists()) { // Look for the descripton.
                    examList.add(new Exam(exam, null));
                    continue;
                }
                BufferedReader description = new BufferedReader(new FileReader(
                        new File(exam, "exam_description.txt")));
                String examDescription = "";
                String temp;
                boolean firstLine = true;
                while((temp = description.readLine()) != null) { // Description is available.
                    if(firstLine) {
                        examDescription += temp;
                        firstLine = false;
                        continue;
                    }
                    examDescription += "\n" + temp;
                }
                examList.add(new Exam(exam, examDescription));
                description.close();
            }
        }
        fileIn.close();

        Exam[] exams = new Exam[examList.size()];
        for(int i = 0; i < examList.size(); i++) { // Convert array list to array.
            exams[i] = examList.get(i);
        }
        oos.writeObject(exams); // Send exam list to the client.
        oos.flush();
    }
}
