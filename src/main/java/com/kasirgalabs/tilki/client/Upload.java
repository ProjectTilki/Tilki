package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.Exam;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class Upload extends Task<Void> {
    private TilkiConnection tilkiConnection;

    @Override
    protected Void call() throws Exception {
        tilkiConnection = new TilkiConnection();
        tilkiConnection.connect();

        FileManager fileManager = FileManager.getInstance();
        List<File> files = fileManager.getFilesForUpload();
        for(File file : files) {
            sendMessage("\tMD5: " + sendFile(file));
        }
        return null;
    }

    private String sendFile(File file) throws IOException, InterruptedException {
        sendMessage("Yükleniyor: " + file.getAbsolutePath());
        handshake(file.getName());
        writeFileToOutput(file);
        String md5Hex = readMD5();
        return md5Hex;
    }

    private void handshake(String fileName) throws IOException {
        DataOutputStream out = tilkiConnection.getDataOutputStream();
        out.writeUTF("Upload");
        out.writeUTF(fileName);
        Exam exam = User.getInstance().getExam();
        out.writeUTF(exam.getName());
    }

    private void writeFileToOutput(File file) throws IOException {
        DataOutputStream out = tilkiConnection.getDataOutputStream();
        FileInputStream fis = new FileInputStream(file);
        try(BufferedInputStream bos = new BufferedInputStream(fis)) {
            int readBytes;
            long writtenBytes = 0;
            byte[] buffer = new byte[8192];
            while((readBytes = bos.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
                writtenBytes += readBytes;
                updateProgress(writtenBytes, file.length());
            }
        }
        out.flush();
        tilkiConnection.shutdownOutput();
    }

    private String readMD5() throws IOException {
        DataInputStream in = tilkiConnection.getDataInputStream();
        return in.readUTF();
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
