import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
    private static final Logger LOG = Logger.getLogger(FoxClientService.class.getName());
    private static final String KEY = "gozetmenSifresi";
    private static final int TIMEOUT = 5000;
    private Socket clientSocket;
    private InputStream socketIn;
    private OutputStream socketOut;
    private InputStream fileIn;
    private OutputStream fileOut;

    /**
     * For the given socket, read operation timeout will be set to 5 seconds.
     *
     * @param socket Incoming connection socket.
     * @param logger Will be used to log connections and status, not
     *               implemented.
     */
    public FoxClientService(Socket socket, Object logger) {
        clientSocket = socket;
        try {
            clientSocket.setSoTimeout(TIMEOUT);
        } catch(SocketException ex) {
            Logger.getLogger(FoxClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads data from an incoming connection. If the authorization is succeeded
     * for client, creates a file related to incoming data, in case of file is
     * already exists, a prefix will be added to the fileName. If the
     * authorization is failed, the connection will be closed.
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
            if(headerLength > 4096) { // Too many bytes for header field are not expected.
                Logger.getLogger(FoxClientService.class.getName()).log(Level.INFO, "Header field is too large, connection closed.");
                clientSocket.close();
                return;
            }
            byte[] data = new byte[headerLength];
            socketIn.read(data);

            int index = 0;

            int keyLength = fetchInt(data, index, index + 4);
            index += 4;
            key = fetchString(data, index, index + keyLength);
            index += keyLength;
            if(!key.equals(KEY)) { // Authorization is failed.
                socketOut.write(ByteBuffer.allocate(4).putInt(0).array());
                socketOut.flush();
                Logger.getLogger(FoxClientService.class.getName()).log(Level.INFO, "Authorization is failed.\nkey: {0}", key);
                clientSocket.close();
            } else { // Authorization is succeeded.
                Logger.getLogger(FoxClientService.class.getName()).log(Level.INFO, "Authorization is succeeded.\nkey: {0}", key);

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

                /*int fileNameLength = fetchInt(data, index, index + 4);
                index += 4;
                fileName = fetchString(data, index, index + fileNameLength);
                index += fileNameLength;*/
                File file = new File(Long.toString(studentId));

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(studentName + "\n");
                bw.write(studentSurname + "\n");
                bw.write(studentId + "\n");

                bw.close();
                /*File file = new File(fileName);
                if(file.exists())
                    for(int i = 0; i < 100; i++) {
                        file = new File(i + "_" + fileName);
                        if(!file.exists()) {
                            fileName = i + "_" + fileName;
                            break;
                        }
                    }
                Logger.getLogger(FoxClientService.class.getName()).log(Level.INFO,
                                                                       "Student information.\n" + "studentName: {0}" + "\n" +
                                                                       "studentSurname: {1}" + "\n" + "studentId: {2}" +
                                                                       "\n" + "fileName: {3}", new Object[]{studentName, studentSurname, studentId, fileName});

                fileOut = new FileOutputStream(fileName);
                int byteCount;
                data = new byte[1024];
                while((byteCount = socketIn.read(data)) > 0)
                    fileOut.write(data, 0, byteCount);
                if(fileOut != null)
                    fileOut.close();

                fileIn = new FileInputStream(fileName);
                byte[] fileData = new byte[1024];
                MessageDigest md = MessageDigest.getInstance("MD5");
                while((byteCount = fileIn.read(fileData)) > 0)
                    md.update(fileData, 0, byteCount);
                byte[] rawChecksum = md.digest();

                StringBuilder md5hex = new StringBuilder();
                for(int i = 0; i < rawChecksum.length; i++)
                    md5hex.append(Integer.toString((rawChecksum[i] & 0xff) + 0x100, 16).substring(1));
                byte[] dataChecksum = md5hex.toString().getBytes(Charset.forName("UTF-8"));
                byte[] checksumLength = ByteBuffer.allocate(4).putInt(dataChecksum.length).array();
                socketOut.write(checksumLength);
                socketOut.write(dataChecksum);
                clientSocket.close();
                Logger.getLogger(FoxClientService.class.getName()).log(Level.INFO, "Section is terminated successfully.");*/
            }
        } catch(IOException ex) {
            Logger.getLogger(FoxClientService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(fileIn != null)
                    fileIn.close();
                if(fileOut != null)
                    fileOut.close();
                if(clientSocket != null)
                    clientSocket.close();
            } catch(IOException ex) {
                Logger.getLogger(FoxClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int fetchInt(byte[] data, int startIndex, int endIndex) {
        if(startIndex == endIndex)
            return 0;
        return ByteBuffer.wrap(Arrays.copyOfRange(data, startIndex, endIndex)).getInt();
    }

    private long fetchLong(byte[] data, int startIndex, int endIndex) {
        if(startIndex == endIndex)
            return 0;
        return ByteBuffer.wrap(Arrays.copyOfRange(data, startIndex, endIndex)).getLong();
    }

    private String fetchString(byte[] data, int startIndex, int endIndex) {
        if(startIndex == endIndex)
            return "";
        byte[] temp = Arrays.copyOfRange(data, startIndex, endIndex);
        return new String(temp, Charset.forName("UTF-8"));
    }
}
