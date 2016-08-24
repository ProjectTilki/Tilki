import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FoxClientFileManager {
    private Socket socket;
    private PrintWriter pw_out;
    private BufferedReader in;
    private OutputStream os_out;

    public FoxClientFileManager() {
        try {
            socket = new Socket("10.5.146.7", 50101);
            pw_out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os_out = socket.getOutputStream();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method for zipping files. On success files will be moved in to a zip.
     * The name of the zip file will be same with the first {@link java.io.File} object
     * specified with the files parameter.
     * @param files array list of {@link java.io.File} objects, which will be zipped.
     * @return returns the name of zip file
     * @throws FileNotFoundException
     * @throws IOException 
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
        for(int i = 0; i < files.length; i++) {
            fileName = files[i].getName();
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) > 0)
                zos.write(bytes, 0, length);
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        fos.close();
        return zipFileName;
    }

    /**
     * Sends a file to a predefined host. On success returns the MD5 checksum of the file.
     * 
     * @param fileName file to be sent.
     * @param id student id
     * @return checksum of the file
     */
    public String sendFiles(String fileName, String id) {
        FileInputStream fileIn = null;
        pw_out.println("Sending file.");
        pw_out.println(fileName);
        pw_out.println(id);
        if(pw_out.checkError()) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, "IOException");
            return null;
        }
        try {
            fileIn = new FileInputStream(fileName);
        } catch(FileNotFoundException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        int bytesCount = 0;
        byte[] fileData = new byte[1024];
        do {
            try {
                bytesCount = fileIn.read(fileData);
                if(bytesCount > 0)
                    os_out.write(fileData, 0, bytesCount);
            } catch(IOException ex) {
                Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } while(bytesCount > 0);
        String checksum;
        try {
            checksum = in.readLine();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientEnrollment.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        try {
            fileIn.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch(IOException ex) {
            Logger.getLogger(FoxClientFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checksum;
    }
}
