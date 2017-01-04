package com.kasirgalabs.tilki.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/*
 * This service compares exam key received from the user with the original
 * exam key and than sends the result to the user.
 */
public class KeyVerifyRefactored extends DefaultService {

    // Since there is only one ExamManger store it as static.
    private static final ExamManager EXAM_MANAGER = ExamManager.getInstance();

    public KeyVerifyRefactored(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void serve() throws IOException {
        try {
            String examName = in.readUTF();
            char[] key = EXAM_MANAGER.getExamKey(examName);
            if(key == null) { // If key is null we don't want to proceed.
                return;
            }
            char[] receivedKey = readChars(key.length);
            out.writeBoolean(Arrays.equals(key, receivedKey));
        }
        catch(EOFException ex) {
            // We got this exception beacuse we tried to read more data
            // than user send. It's okay to ignore this one since user
            // does not know how many characters key has. (S)he may tried to send
            // shorter key. It means that orginal key length did not
            // match with the one user send. So do not accept.
            out.writeBoolean(false);
        }
        catch(IOException ex) {
            out.writeBoolean(false);
            throw ex;
        }
        finally {
            out.flush(); // Just in case.
            socket.close();
        }
    }

    /**
     * Reads chars from the socket. The number of chars read from the socket is
     * specified by the parameter <code>count</code>.
     *
     * @param count Number of chars to be read.
     *
     * @return Chars read from socket. If number of read chars exceed
     *         <code>count</code> returns null.
     *
     * @throws EOFException If the input stream reaches the end before
     *                      reading specified number of chars.
     *
     * @throws IOException  If an I/O error occurs.
     */
    private char[] readChars(int count) throws EOFException, IOException {
        char[] receivedKey = new char[count];
        for(int i = 0; i < count; i++) { // Read chars.
            receivedKey[i] = in.readChar();
        }

        // Check if user is still sending data.
        // If then it's exceeded the key length,
        // do not accept.
        if(in.read() != -1) {
            return null;
        }
        return receivedKey;
    }
}
