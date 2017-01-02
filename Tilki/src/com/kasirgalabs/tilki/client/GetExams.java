package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.Exam;
import com.kasirgalabs.tilki.utils.ExamList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GetExams extends DefaultService<ServiceListener<ExamList>, ExamList> {

    public GetExams(ArrayList<ServiceListener<ExamList>> listeners) {
        super(listeners);
    }

    /**
     * Requests the available exam list from a predefined host.
     *
     * @return Each element of the array list represents an exam. Returns null
     *         if there is no exam.
     *
     * @throws java.lang.ClassNotFoundException Class of a serialized object
     *                                          cannot be found.
     * @throws java.io.IOException              Any of the usual I/O related
     *                                          exceptions.
     * @see Exam
     */
    @Override
    public ExamList call() throws IOException, ClassNotFoundException {
        Exam[] examList = null;
        Socket socket = null;
        try {
            socket = new Socket("localhost", 50101);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("List exams."); // Tell host which operation will occur.
            out.flush();

            ObjectInputStream ois = new ObjectInputStream(
                    socket.getInputStream());
            Object examListObject = ois.readObject();
            examList = (Exam[]) examListObject;
        }
        catch(IOException | ClassNotFoundException e) {
            throw e;
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
        ExamList temp = new ExamList();
        for(Exam exam : examList) {
            temp.add(exam);
        }
        return temp;
    }
}
