import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Fox Project zipping utilities.
 */
public class FoxZip {
    /**
     * The createZipFile method creates a FileOutputStream to a zip file, the
     * name of the zip file determined by the first index of the parameter. It
     * creates a ZipOutputStream object based on the FileOutputStream.
     *
     * @param files an array of {@link java.io.File} objects, which will be
     *              zipped.
     *
     * @return the zip file object.
     *
     * @throws FileNotFoundException an attempt to open the file denoted by a
     *                               specified pathname has failed.
     * @throws IOException           an I/O error has occurred.
     */
    public File createZipFile(File[] files) throws FileNotFoundException,
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

        return new File(zipFileName);
    }
}
