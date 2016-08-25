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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FoxClientFileManager {
    
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
        for(File file1 : files) {
            fileName = file1.getName();
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
     * @throws java.io.IOException
     */
    public String sendFiles(String fileName, String id) throws IOException {
        Socket socket = new Socket("localhost", 50101);
        PrintWriter pw_out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream os_out = socket.getOutputStream();
        pw_out.println("Sending file.");
        pw_out.println(fileName);
        pw_out.println(id);
        FileInputStream fileIn = new FileInputStream(fileName);
        int bytesCount;
        byte[] fileData = new byte[1024];
        do {
            bytesCount = fileIn.read(fileData);
            if(bytesCount > 0)
                os_out.write(fileData, 0, bytesCount);
        } while(bytesCount > 0);
        String checksum;
        checksum = in.readLine();
        fileIn.close();
        socket.close();
        return checksum;
    }
}
