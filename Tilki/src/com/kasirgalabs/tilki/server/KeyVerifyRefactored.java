package com.kasirgalabs.tilki.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class KeyVerifyRefactored extends DefaultService {

    public KeyVerifyRefactored(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void serve() throws IOException {
        try {
            String examName = in.readUTF();
            ExamManager examManager = ExamManager.getInstance();
            if(examManager.isExamExists(examName)) {
                char[] key = examManager.getExamKey(examName);
                char[] temp = new char[key.length];
                for(int i = 0; i < temp.length; i++) {
                    temp[i] = in.readChar();
                }
                if(in.read() != -1) {
                    out.writeBoolean(false);
                    return;
                }
                if(Arrays.equals(key, temp)) {
                    out.writeBoolean(true);
                    return;
                }
            }
            out.writeBoolean(false);
        }
        catch(EOFException ex) {
            out.writeBoolean(false);
        }
        catch(IOException ex) {
            throw ex;
        }
        finally {
            out.flush();
            socket.close();
        }
    }
}
