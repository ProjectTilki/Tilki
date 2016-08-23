package jxCapture;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private String sourceFilePath = "C:\\Users\\ONURAL\\Desktop\\WebCamera.wmv";
    private FileEvent fileEvent = null;
    private String destinationPath = "C:\\Users\\ONURAL\\Desktop\\Temps\\";

    public Client() {

    }

    /**
     * Server calisiyorken lokalden veya baska bir ip den baglanti kur.
     */
    public void connect() {
        while (!isConnected) {
            try {
                socket = new Socket("localHost", 4445);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setSourcePath(String a){
    	this.sourceFilePath = a;
    }

   /**
     * FileEvent Nesnesini Gonderir.
     */
    public void sendFile() {
        fileEvent = new FileEvent();
        String fileName = "WebCamera.wmv";
        String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
        fileEvent.setDestinationDirectory(destinationPath);       //Final Dest.
        fileEvent.setFilename(fileName);
        fileEvent.setSourceDirectory(sourceFilePath);      //Source
        File file = new File(sourceFilePath);              //Source
        if (file.isFile()) {
            try {
            	DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setFileSize(len);
                fileEvent.setFileData(fileBytes);
                fileEvent.setStatus("Success");
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("path specified is not pointing to a file");
            fileEvent.setStatus("Error");
        }
        //su an FileEvent Nesnesini sokete yaziyor.
        try {
            outputStream.writeObject(fileEvent);
            System.out.println("Done...Going to exit");
            Thread.sleep(3000);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static void clie(){
    	  Client client = new Client();
      	
	        client.connect();
		
	        client.sendFile();
    }
    public static void main(String[] args) {
    		
    	        Client client = new Client();
    	
    	        client.connect();
    		
    	        client.sendFile();
    		
    	    }
    		
    	}

    
        
    


