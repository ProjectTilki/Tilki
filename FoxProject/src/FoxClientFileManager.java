import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class consists methods that operate on files and send them over the
 * socket. File sending operation on this class uses {@link java.net.Socket}
 * object to send data to a predefined host. Invoking
 * {@link #createZipFile(java.io.File[]) createZipFile(File[] files)} method may
 * modify some files.
 * <p>
 * This class does not have any instance variables.
 */
public class FoxClientFileManager {

    /**
     * Creates a zip file on the current working directory which contains
     * file(s) specified with the parameter. Original files will not be moved
     * and their contents will not be changed.
     * <p>
     * The name of the zip file will be same with the first {@link java.io.File}
     * object's name specified with the parameter, file name must has a period
     * before it's file name extension, if not
     * {@link java.io.FileNotFoundException} will be thrown. If the file already
     * exists it will be overwritten.
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
     * object . On success returns the MD5 checksum of the file.
     *
     * @param fileName Relative path name of the file for current working
     *                 directory.
     * @param id       The student id.
     * @param exam     The exam name.
     *
     * @return Checksum of the file.
     *
     * @throws java.io.IOException If an I/O error occurs.
     */
    public String sendFile(String fileName, String id, String exam) throws IOException {
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
        FileInputStream fileIn = new FileInputStream(fileName);
        OutputStream os_out = socket.getOutputStream();
        int bytesCount;
        byte[] fileData = new byte[1024];
        do {
            bytesCount = fileIn.read(fileData);
            if(bytesCount > 0)
                os_out.write(fileData, 0, bytesCount);
        }while(bytesCount > 0);
        os_out.flush();
        socket.shutdownOutput(); // Shut down output to tell server no more data.
        String checksum = in.readUTF(); // Read checksum from socket.

        fileIn.close();
        socket.close();
        return checksum;
    }
}
