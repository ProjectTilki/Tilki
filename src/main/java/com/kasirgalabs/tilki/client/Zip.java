package com.kasirgalabs.tilki.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class Zip extends Task<Void> {
    private FileManager fileManager;

    @Override
    protected Void call() throws Exception {
        fileManager = FileManager.getInstance();
        zipUserFiles();
        return null;
    }

    private void zipUserFiles() throws IOException, InterruptedException {
        List<File> files = fileManager.getTrackedUserFiles();
        String id = User.getInstance().getId();
        String zipFileName = fileManager.generateFileName("user" + id + ".zip");
        createZipFile(zipFileName, files);
        fileManager.addFileForUpload(new File(zipFileName));
        sendMessage("\tZip: " + zipFileName);
    }

    private void createZipFile(String zipFileName, List<File> files) throws IOException, InterruptedException {
        FileOutputStream fos = new FileOutputStream(zipFileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        try(ZipOutputStream zos = new ZipOutputStream(bos)) {
            for(File file : files) {
                addFileToZip(zos, file);
            }
        }
    }

    private void addFileToZip(ZipOutputStream zos, File file) throws IOException, InterruptedException {
        sendMessage("Zipleniyor: " + file.getAbsolutePath());
        createEntry(zos, file.getName());
        FileInputStream fis = new FileInputStream(file);
        try(BufferedInputStream bos = new BufferedInputStream(fis)) {
            int readBytes;
            long writtenBytes = 0;
            byte[] buffer = new byte[8192];
            while((readBytes = bos.read(buffer)) > 0) {
                zos.write(buffer, 0, readBytes);
                writtenBytes += readBytes;
                updateProgress(writtenBytes, file.length());
            }
            zos.closeEntry();
        }
    }

    private void createEntry(ZipOutputStream zos, String name) throws IOException {
        ZipEntry zipEntry = new ZipEntry(name);
        zos.putNextEntry(zipEntry);
    }

    private void sendMessage(String message) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            updateMessage(message);
            latch.countDown();
        });
        latch.await();
    }
}
