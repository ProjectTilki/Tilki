import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoxClientService is the class for handling incoming connections. On failure,
 * connection will be closed, main program will not be affected.
 * <p>
 * Implements method from: {@link java.lang.Runnable}.
 */
public class FoxClientService implements Runnable {

    private Socket clientSocket;
    private InputStream socketIn;
    private OutputStream socketOut;
    private InputStream fileIn;
    private OutputStream fileOut;
    private static final String KEY = "test0";

    /**
     *
     * @param socket Incoming connection socket.
     */
    public FoxClientService(Socket socket) {
        clientSocket = socket;
    }

    /**
     * Reads data from an incoming connection. If authorization succeeded for
     * client, creates a file related to incoming data, in case of file is
     * already exists, some numbers will be appended to the fileName. If authorization is failed, the
     * connection will be closed.
     * <p>
     * New thread starts code execution on this method.
     */
    public void run() {
        try {
            String key;
            String studentName;
            String studentSurname;
            long studentId;
            String fileName;

            socketIn = clientSocket.getInputStream();
            socketOut = clientSocket.getOutputStream();

            byte[] header = new byte[4];
            socketIn.read(header);

            int headerLength = fetchInt(header, 0, 4);
            byte[] data = new byte[headerLength];
            socketIn.read(data);

            int index = 0;

            int keyLength = fetchInt(data, index, index + 4);
            index += 4;
            key = fetchString(data, index, index + keyLength);
            index += keyLength;
            System.out.println("key: " + key);
            if(!key.equals(KEY)) {
                socketOut.write(ByteBuffer.allocate(4).putInt(0).array());
                socketOut.flush();
            }else {
                socketOut.write(ByteBuffer.allocate(4).putInt(1).array());
                socketOut.flush();
                int studentNameLength = fetchInt(data, index, index + 4);
                index += 4;
                studentName = fetchString(data, index, index + studentNameLength);
                index += studentNameLength;

                int studentSurnameLength = fetchInt(data, index, index + 4);
                index += 4;
                studentSurname = fetchString(data, index, index + studentSurnameLength);
                index += studentSurnameLength;

                studentId = fetchLong(data, index, index + 8);
                index += 8;

                int fileNameLength = fetchInt(data, index, index + 4);
                index += 4;
                fileName = fetchString(data, index, index + fileNameLength);
                index += fileNameLength;

                System.out.println("studentName: " + studentName);
                System.out.println("studentSurname: " + studentSurname);
                System.out.println("studentId: " + studentId);
                System.out.println("fileName: " + fileName);

                File file = new File(fileName);
                String fileExtension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                if(file.exists()) {
                    String temp = "";
                    for(int i = 0; i < 10; i++) {
                        int pos = fileName.lastIndexOf(".");
                        if(pos > 0) {
                            temp = fileName.substring(0, pos);
                        }
                        file = new File(temp + "_" + i + fileExtension);
                        if(!file.exists()) {
                            fileName = temp + "_" + i + fileExtension;
                            break;
                        }
                    }
                }
                fileOut = new FileOutputStream(fileName);

                int byteCount;
                data = new byte[1024];
                while((byteCount = socketIn.read(data)) > 0) {
                    fileOut.write(data, 0, byteCount);
                }
                if(fileOut != null) {
                    fileOut.close();
                }

                fileIn = new FileInputStream(fileName);
                byte[] fileData = new byte[1024];
                MessageDigest md = MessageDigest.getInstance("MD5");
                while((byteCount = fileIn.read(fileData)) > 0) {
                    md.update(fileData, 0, byteCount);
                }
                byte[] rawChecksum = md.digest();

                StringBuilder md5hex = new StringBuilder();
                for(int i = 0; i < rawChecksum.length; i++) {
                    md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100, 16).substring(1));
                }
                byte[] dataChecksum = md5hex.toString().getBytes(Charset.forName("UTF-8"));
                byte[] checksumLength = ByteBuffer.allocate(4).putInt(dataChecksum.length).array();
                socketOut.write(checksumLength);
                socketOut.write(dataChecksum);
            }
            clientSocket.close();
        }catch(IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(FoxClientService.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if(fileIn != null) {
                    fileIn.close();
                }
                if(fileOut != null) {
                    fileOut.close();
                }
                if(clientSocket != null) {
                    clientSocket.close();
                }
            }catch(IOException ex) {
                Logger.getLogger(FoxClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int fetchInt(byte[] data, int startIndex, int endIndex) {
        return ByteBuffer.wrap(Arrays.copyOfRange(data, startIndex, endIndex)).getInt();
    }

    private long fetchLong(byte[] data, int startIndex, int endIndex) {
        return ByteBuffer.wrap(Arrays.copyOfRange(data, startIndex, endIndex)).getLong();
    }

    private String fetchString(byte[] data, int startIndex, int endIndex) {
        byte[] temp = Arrays.copyOfRange(data, startIndex, endIndex);
        return new String(temp, Charset.forName("UTF-8"));
    }
}
