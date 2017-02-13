/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.Exam;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javafx.concurrent.Task;

public class GetExams extends Task<Exam[]> {

    @Override
    protected Exam[] call() throws Exception {
        Exam[] exams = null;
        Socket socket = null;
        try {
            socket = new Socket("localhost", 50101);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("List exams."); // Tell host which operation will occur.
            out.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object examListObject = ois.readObject();
            exams = (Exam[]) examListObject;
            return exams;
        } catch(IOException | ClassNotFoundException ex) {
            //Logger.getLogger(GetExams.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
}
