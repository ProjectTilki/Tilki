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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rootg
 */
public class FoxClientSendFile {
    private Socket clientSocket;
    private InputStream socketIn;
    private OutputStream socketOut;
    private InputStream fileIn;

    public String sendFile(String fileName) throws FileNotFoundException,
                                                   IOException {
        byte[] dataFileName = fileName.getBytes(Charset.forName("UTF-8"));
        byte[] fileNameLength = ByteBuffer.allocate(4).putInt(fileName.length()).array();
        File file = new File(fileName);
        socketIn = clientSocket.getInputStream();
        socketOut = clientSocket.getOutputStream();
        fileIn = new FileInputStream(file);
        socketOut.write(ByteBuffer.allocate(4).putInt(2).array());
        byte[] data = new byte[fileNameLength.length + dataFileName.length + 4];
        int index = 0;
        System.arraycopy(fileNameLength, 0, data, index, fileNameLength.length);
        index += fileNameLength.length;
        System.arraycopy(dataFileName, 0, data, index, dataFileName.length);
        index += dataFileName.length;
        byte[] fileData = new byte[1024];
        int bytesCount;
        do {
            bytesCount = fileIn.read(fileData);
            if(bytesCount > 0)
                socketOut.write(fileData, 0, bytesCount);
        } while(bytesCount > 0);
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
        fileIn.close();
        return checksum;
    }

}
