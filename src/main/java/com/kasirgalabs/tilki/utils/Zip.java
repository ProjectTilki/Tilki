package com.kasirgalabs.tilki.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Callable<Void> {

    private final LinkedBlockingDeque<ZipResult> result;
    private final String zipFileName;
    private final File[] files;

    public Zip(String zipFileName, File[] files) {
        this.zipFileName = zipFileName;
        this.files = files;
        result = new LinkedBlockingDeque<>();
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
    private void createZipFile() throws IOException {
        ZipOutputStream zos = null;

        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);

            for(File file : files) {
                addFileToZip(zos, file);
            }
        } catch(IOException ex) {
            throw ex;
        }
        finally {
            if(zos != null) {
                zos.close();
            }
        }
    }

    private void addFileToZip(ZipOutputStream zos, File file) throws IOException {
        BufferedInputStream bos = null;

        try {
            createEntry(zos, file);

            FileInputStream fis = new FileInputStream(file);
            bos = new BufferedInputStream(fis);

            long fileSize = file.length();
            long writtenBytes = 0;
            int readBytes;
            byte[] buffer = new byte[8192];

            while((readBytes = bos.read(buffer)) > 0) {
                zos.write(buffer, 0, readBytes);
                writtenBytes += readBytes;
                updateProgress(file, writtenBytes, fileSize);
            }
            updateProgress(file, writtenBytes, fileSize);
            zos.closeEntry();
        } catch(IOException ex) {
            throw ex;
        }
        finally {
            if(bos != null) {
                bos.close();
            }
        }
    }

    private void createEntry(ZipOutputStream zos, File file) throws IOException {
        String fileName = file.getName();
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
    }

    private void updateProgress(File file, long writtenBytes, long fileSize) {
        if(fileSize == 0) {
            result.offerLast(new ZipResult(file, 100));
            return;
        }

        ZipResult lastResult = result.peekLast();
        ZipResult currentResult = new ZipResult(file,
            (int) ((double) writtenBytes / fileSize * 100));

        if(lastResult == null) {
            result.offerLast(currentResult);
            return;
        }
        if(!lastResult.equals(currentResult)) {
            result.offerLast(currentResult);
        }
    }

    public ZipResult getProgress(long timeout, TimeUnit unit) throws InterruptedException {
        return result.pollFirst(timeout, unit);
    }

    @Override
    public Void call() throws Exception {
        createZipFile();
        return null;
    }
}
