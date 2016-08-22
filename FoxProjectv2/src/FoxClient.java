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
    public static final String FILE_FNFE = "1";
    /**
     * {@link java.lang.SecurityException} is thrown by
     * {@link java.io.FileInputStream} object.
     */
    public static final String FILE_IOE = "2";

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
            clientSocket = new Socket("10.5.147.145", 50101);
        } catch(IOException ioe) {
            throw new FoxException(FoxClient.SOCKET_IOE, ioe);
        }
    }

    /**
     * Sends data to the predefined host. On success, returns the MD5 checksum
     * of the file specified with the fileName parameter, if the authorization
     * is failed, returns null.
     *
     * @param key            the authorization key.
     * @param studentName    the student name.
     * @param studentSurname the student surname.
     * @param studentId      the student id.
     * @param fileName       the file name including file extension.
     *
     * @return MD5 checksum of the file specified with the fileName.
     *
     * @throws FoxException On failure, an exception is thrown and exception
     *                      message set to indicate the error. On failure,
     *                      {@link java.net.Socket} object will be closed.
     */
    public String connectToServer(String key, String studentName, String studentSurname,
                                  long studentId, String fileName)
            throws FoxException, IOException {
        try {
            byte[] dataKey = key.getBytes(Charset.forName("UTF-8"));
            byte[] keyLength = ByteBuffer.allocate(4).putInt(dataKey.length).array();

            byte[] dataStudentName = studentName.getBytes(Charset.forName("UTF-8"));
            byte[] studentNameLength = ByteBuffer.allocate(4).putInt(dataStudentName.length).array();

            byte[] dataStudentSurname = studentSurname.getBytes(Charset.forName("UTF-8"));
            byte[] studentSurnameLength = ByteBuffer.allocate(4).putInt(dataStudentSurname.length).array();

            byte[] dataStudentId = ByteBuffer.allocate(8).putLong(studentId).array();

            /*byte[] dataFileName = fileName.getBytes(Charset.forName("UTF-8"));
            byte[] fileNameLength = ByteBuffer.allocate(4).putInt(fileName.length()).array();*/
            File file = new File(fileName);
            errorType = SOCKET_ERROR;
            socketIn = clientSocket.getInputStream();
            socketOut = clientSocket.getOutputStream();
            errorType = FILE_ERROR;
            fileIn = new FileInputStream(file);

            int index = 0;

            byte[] data = new byte[keyLength.length + dataKey.length + studentNameLength.length + dataStudentName.length +
                                   studentSurnameLength.length + dataStudentSurname.length +
                                   dataStudentId.length + 4];

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

            /*System.arraycopy(fileNameLength, 0, data, index, fileNameLength.length);
            index += fileNameLength.length;
            System.arraycopy(dataFileName, 0, data, index, dataFileName.length);
            index += dataFileName.length;*/
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
            } else
                // Authentication is failed.
                return null;
        } catch(FileNotFoundException fnfe) {
            throw new FoxException(FoxClient.FILE_FNFE, fnfe);
        } catch(IOException ioe) {
            if(errorType == SOCKET_ERROR)
                throw new FoxException(FoxClient.SOCKET_IOE, ioe);
            else
                throw new FoxException(FoxClient.FILE_IOE, ioe);
        }
    }
}
