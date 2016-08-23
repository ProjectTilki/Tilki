package jxCapture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
    private FileOutputStream fileOutputStream = null;

   public Server() {

    }

    /**
     * Accepts socket connection
     */
    public void doConnect() {
        try {
            serverSocket = new ServerSocket(4445);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Reading the FileEvent object and copying the file to disk.
     */
    public void downloadFile() {
        try {
            fileEvent = (FileEvent) inputStream.readObject();
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
                System.out.println("Error occurred ..So exiting");
                System.exit(0);
            }
            String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                new File(fileEvent.getDestinationDirectory()).mkdirs();
            }
            dstFile = new File(outputFile);
            fileOutputStream = new FileOutputStream(dstFile);
            fileOutputStream.write(fileEvent.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Output file : " + outputFile + " is successfully saved ");
            Thread.sleep(3000);
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void serve(){
    	Server server = new Server();
        server.doConnect();
        server.downloadFile();
    }
    
    public static void main(String[] args) {
       
    	Server server = new Server();
        server.doConnect();
        server.downloadFile();
    }
}
