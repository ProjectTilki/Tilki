import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * The class FoxClient contains a method for connecting and sending data to a
 * predefined host.
 */
public class FoxClient {

    private static int errorType;
    private static final int SOCKET_ERROR = 0;
    private static final int FILE_ERROR = 1;

    // Exception error codes.
    /**
     * {@link java.io.IOException} is thrown by {@link java.net.Socket} object.
     */
    public static final String SOCKET_IOE = "0";
    /**
     * {@link java.lang.SecurityException} is thrown by {@link java.net.Socket}
     * object.
     */
    public static final String SOCKET_SE = "1";
    /**
     * {@link java.io.FileNotFoundException} is thrown by
     * {@link java.io.FileInputStream} object.
     */
    public static final String FILE_FNFE = "2";
    /**
     * {@link java.lang.SecurityException} is thrown by
     * {@link java.io.FileInputStream} object.
     */
    public static final String FILE_SE = "3";
    /**
     * {@link java.io.IOException} is thrown by {@link java.io.FileInputStream}
     * object.
     */
    public static final String FILE_IOE = "4";

    private Socket clientSocket;
    private InputStream socketIn;
    private OutputStream socketOut;
    private InputStream fileIn;

    /**
     * Creates a {@link java.net.Socket} object and connects it to the
     * predefined remote host.
     *
     * @throws FoxException On failure, exception message will be set to
     *                      indicate the error.
     */
    public FoxClient() throws FoxException {
        try {
            clientSocket = new Socket("127.0.0.1", 44101);
        }catch(IOException ioe) {
            throw new FoxException(FoxClient.SOCKET_IOE, ioe);
        }catch(SecurityException se) {
            throw new FoxException(FoxClient.SOCKET_SE, se);
        }
    }

    /**
     * Sends data to predefined host. On success, returns the MD5 checksum of
     * the file specified with the fileName parameter, if authorization is
     * failed, returns null.
     *
     * @param key            the authorization key.
     * @param studentName    the student name.
     * @param studentSurname the student surname.
     * @param studentId      the student id.
     * @param fileName       the file name including file extension.
     *
     * @throws FoxException On failure, an exception is thrown and exception
     *                      message set to indicate the error. On failure, {@link java.net.Socket} object will be
     *                      closed.
     */
    public String connectToServer(String key, String studentName, String studentSurname,
                                  long studentId, String fileName)
            throws FoxException {
        byte[] dataKey = key.getBytes(Charset.forName("UTF-8"));
        byte[] keyLength = ByteBuffer.allocate(4).putInt(dataKey.length).array();

        byte[] dataStudentName = studentName.getBytes(Charset.forName("UTF-8"));
        byte[] studentNameLength = ByteBuffer.allocate(4).putInt(dataStudentName.length).array();

        byte[] dataStudentSurname = studentSurname.getBytes(Charset.forName("UTF-8"));
        byte[] studentSurnameLength = ByteBuffer.allocate(4).putInt(dataStudentSurname.length).array();

        byte[] dataStudentId = ByteBuffer.allocate(8).putLong(studentId).array();

        byte[] dataFileName = fileName.getBytes(Charset.forName("UTF-8"));
        byte[] fileNameLength = ByteBuffer.allocate(4).putInt(fileName.length()).array();

        File file = new File(fileName);
        try {
            fileIn = new FileInputStream(file);
            socketIn = clientSocket.getInputStream();
            socketOut = clientSocket.getOutputStream();
        }catch(FileNotFoundException fnfe) {
            throw new FoxException(FoxClient.FILE_FNFE, fnfe);
        }catch(SecurityException se) {
            throw new FoxException(FoxClient.FILE_SE, se);
        }catch(IOException ioe) {
            throw new FoxException(FoxClient.SOCKET_IOE, ioe);
        }

        int index = 0;

        byte[] data = new byte[keyLength.length + dataKey.length + studentNameLength.length + dataStudentName.length
                               + studentSurnameLength.length + dataStudentSurname.length
                               + dataStudentId.length + fileNameLength.length
                               + dataFileName.length + 4];

        byte[] dataLength = ByteBuffer.allocate(4).putInt(data.length - 4).array();

        System.arraycopy(dataLength, 0, data, index, dataLength.length);
        index += dataLength.length;

        System.arraycopy(keyLength, 0, data, index, keyLength.length);
        index += keyLength.length;
        System.arraycopy(dataKey, 0, data, index, dataKey.length);
        index += dataKey.length;

        System.arraycopy(studentNameLength, 0, data, index, studentNameLength.length);
        index += studentNameLength.length;
        System.arraycopy(dataStudentName, 0, data, index, dataStudentName.length);
        index += dataStudentName.length;

        System.arraycopy(studentSurnameLength, 0, data, index, studentSurnameLength.length);
        index += studentSurnameLength.length;
        System.arraycopy(dataStudentSurname, 0, data, index, dataStudentSurname.length);
        index += dataStudentSurname.length;

        System.arraycopy(dataStudentId, 0, data, index, dataStudentId.length);
        index += dataStudentId.length;

        System.arraycopy(fileNameLength, 0, data, index, fileNameLength.length);
        index += fileNameLength.length;
        System.arraycopy(dataFileName, 0, data, index, dataFileName.length);
        index += dataFileName.length;

        try {
            errorType = SOCKET_ERROR;
            socketOut.write(data, 0, data.length);
            socketOut.flush();

            byte[] authentication = new byte[4];
            socketIn.read(authentication);
            if(ByteBuffer.wrap(Arrays.copyOfRange(authentication, 0, 4)).getInt() == 0) {
                clientSocket.close();
                errorType = FILE_ERROR;
                fileIn.close();
                return null;
            }else {
                int bytesCount;
                byte[] fileData = new byte[1024];
                do {
                    errorType = FILE_ERROR;
                    bytesCount = fileIn.read(fileData);
                    errorType = SOCKET_ERROR;
                    if(bytesCount > 0) {
                        socketOut.write(fileData, 0, bytesCount);
                    }
                }while(bytesCount > 0);
                clientSocket.shutdownOutput();

                byte[] checksumHeader = new byte[4];
                socketIn.read(checksumHeader);
                int checksumLength = ByteBuffer.wrap(Arrays.copyOfRange(checksumHeader, 0, 4)).getInt();

                byte[] dataChecksum = new byte[checksumLength];
                socketIn.read(dataChecksum);
                byte[] temp = Arrays.copyOfRange(dataChecksum, 0, checksumLength);
                String checksum = new String(temp, Charset.forName("UTF-8"));
                System.out.println(checksum);

                clientSocket.close();
                errorType = FILE_ERROR;
                fileIn.close();
                return checksum;
            }
        }catch(IOException ioe) {
            if(errorType == SOCKET_ERROR) {
                throw new FoxException(FoxClient.SOCKET_IOE, ioe);
            }else {
                throw new FoxException(FoxClient.FILE_IOE, ioe);
            }
        }
    }
}
