import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class contains methods for FoxProject's client side application. Main
 * purpose of this class is to satisfy the requirements of FoxProject.
 * <p>
 * This class is used to send usage data to a remote host along with the user
 * information. Some methods in this class are used to establish a connection
 * with a remote host, modify and send files, they can also be used to verify
 * the name, surname and id for an exam attendance of a student.
 * <p>
 * Some methods use {@link java.net.Socket} to connect to a remote host and may
 * send or receive data.<br>
 * {@link #createZipFile(java.io.File[]) createZipFile(File[] files)} method may
 * modify some files.
 */
public class FoxClientUtilities {
    private String name = null;
    private String surname = null;
    private String id = null;
    private String exam = null;

    /**
     * Check in a student or establish a reconnection to a predefined host. This
     * method uses {@link java.net.Socket} class to connect to a host.
     * <p>
     * Method parameters are saved in instance variables for further use.
     *
     * @param name    The student name.
     * @param surname The student surname.
     * @param id      The student id.
     * @param exam    The exam name.
     *
     * @return The status of the check in; returns 0 if exam folder is missing
     *         in remote host, 1 if reconnection is successful, 2 if check in is
     *         successful, returns -1 if an error occurred.
     *
     * @throws java.io.IOException If an I/O error occurs when creating socket,
     *                             reading from socket, or sending to socket.
     */
    public int checkIn(String name, String surname, String id, String exam)
            throws IOException {
        // Save informations to instance variables.
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.exam = exam;

        Socket socket = new Socket("localhost", 50101); // Connect to the host.
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Check in."); // Tell host which operation will occur.

        // Send informations to the host.
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.flush();

        String status = in.readUTF(); // Read status code of check in.
        if(status != null) {
            char c = status.charAt(0);
            return Character.getNumericValue(c);
        }
        socket.close();
        return -1;
    }

    /**
     * Verifies the enrollment of a student by connecting to a predefined host
     * This method uses {@link java.net.Socket} object to connect to the host.
     * <p>
     * Implementation note: Student informations are stored in instance
     * variables. Before using this method,
     * {@link #checkIn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * method must be used to store them. If not, a
     * {@link java.lang.NullPointerException} will be thrown.
     *
     * @param instructorKey The instructor key
     *
     * @return Returns 0 if exam folder or key file is missing in remote host,
     *         returns 1 if key is not accepted, returns 2 if key is accepted,
     *         returns -1 if an error is occurred.
     *
     * @throws java.io.IOException If an I/O error occurs when creating socket,
     *                             reading from socket, or sending to socket.
     */
    public int verifyInstructorKey(String instructorKey) throws IOException {
        if(name == null || surname == null || id == null || exam == null || instructorKey == null)
            throw new NullPointerException();
        Socket socket = new Socket("localhost", 50101); // Connect to the host.
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Key verify."); // Tell host which operation will occur.

        // Send informations.
        out.writeUTF(name);
        out.writeUTF(surname);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.writeUTF(instructorKey);
        out.flush();

        String status = in.readUTF(); // Read the status code.
        if(status != null) {
            char c = status.charAt(0);
            return Character.getNumericValue(c);
        }
        socket.close();
        return -1;
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
    public Exam[] availableExams() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 50101); // Connect to the host.
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("List exams."); // Tell host which operation will occur.
        out.flush();

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object examListObject = ois.readObject();
        Exam[] examList = (Exam[]) examListObject;
        return examList;
    }

    /**
     * Creates a zip file on the current working directory which contains
     * file(s) specified with the parameter. Original files will not be moved
     * and their contents will not be changed.
     * <p>
     * The name of the zip file will be same with the first {@link java.io.File}
     * object's name specified with the parameter. If the file already exists it
     * will be overwritten.
     *
     * @param files Array list of {@link java.io.File} objects, which will be
     *              zipped. All files must be in the current working directory.
     *
     * @return The name of the zip file.
     *
     * @throws FileNotFoundException File with the specified pathname does not
     *                               exist or is a directory.
     * @throws IOException           If an I/O error occurs.
     */
    public String createZipFile(File[] files) throws FileNotFoundException,
                                                     IOException {
        String zipFileName = files[0].getName();
        int pos = zipFileName.lastIndexOf('.');
        if(pos > 0)
            zipFileName = zipFileName.substring(0, pos) + ".zip";
        else
            zipFileName = zipFileName + ".zip";
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        String fileName;
        for(File file : files) {
            fileName = file.getName();
            File firstFile = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[4096];
            int length;
            while((length = fis.read(buffer)) > 0)
                zos.write(buffer, 0, length);
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        fos.close();
        return zipFileName;
    }

    /**
     * Sends a file to a predefined host by creating a {@link java.net.Socket}
     * object. On success returns the MD5 checksum of the file.
     *
     * @param fileName Relative path name of the file for current working
     *                 directory.
     * @param id       The student id.
     * @param exam     The exam name.
     * @param object
     *
     * @return Checksum of the file.
     *
     * @throws java.io.FileNotFoundException If the file does not exist, is a
     *                                       directory rather than a regular
     *                                       file, or for some other reason
     *                                       cannot be opened for reading.
     * @throws java.lang.SecurityException   If a security manager exists and
     *                                       its checkRead method denies read
     *                                       access to the file.
     * @throws java.io.IOException           If an I/O error occurs.
     */
    public String sendFile(String fileName, String id, String exam,
                           Object object) throws FileNotFoundException, SecurityException, IOException {
        // Create a socket and initialize it's streams.
        Socket socket = new Socket("localhost", 50101);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Sending file.");
        out.writeUTF(fileName);
        out.writeUTF(id);
        out.writeUTF(exam);
        out.flush();

        // Read file and send it over the socket.
        long fileSize = new File(fileName).length();
        FileInputStream fileIn = new FileInputStream(fileName);
        OutputStream os_out = socket.getOutputStream();
        long sentBytes = 0;
        long progress = 0;
        int bytesCount;
        byte[] fileData = new byte[1024];
        do {
            bytesCount = fileIn.read(fileData);
            if(bytesCount > 0) {
                os_out.write(fileData, 0, bytesCount);
                sentBytes += bytesCount;
                if(sentBytes >= fileSize / 100) {
                    sentBytes = 0;
                    progress++;
                }
            }
        }while(bytesCount > 0);
        if(progress != 100)
            progress++;
        os_out.flush();
        socket.shutdownOutput(); // Shut down output to tell server no more data.
        String checksum = in.readUTF(); // Read checksum from socket.

        fileIn.close();
        socket.close();
        return checksum;
    }
}
