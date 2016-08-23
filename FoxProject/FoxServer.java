import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * FoxServer class implements an application that accepts connection from clients
 * and receives data. This class uses {@link java.net.ServerSocket} class to accept
 * incoming connections. Every incoming connection will be handled by a thread.
 * The maximum queue length for incoming connection is 200. If a connection arrives
 * when the queue is full, the connection refused.
 */
public class FoxServer {
    private static ServerSocket serverSocket = null;
    private static ArrayList<Thread> threadList = new ArrayList<Thread>();

    /**
     * @param args Not used.
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("\nRunning shutdown hook...");
                
                for(Thread thread: threadList) {
                    if(thread.isAlive())
                        thread.interrupt();
                }
                
                try {
                    if(serverSocket != null)
                        serverSocket.close();
                }catch (IOException ex1) {
                    System.err.println("An I/O error occured when closing the server socket.");
                }
                
                System.err.println("Shutdown hook is completed.");
            }
        });
        
        try {
            serverSocket = new ServerSocket(44101, 200);
        }catch(IOException ioe) {
            ioe.printStackTrace();
            System.err.println("An I/O error occured when opening the socket.");
            System.exit(0);
        }catch(SecurityException se) {
            se.printStackTrace();
            System.err.println("Socket can not be opened, permission denied.");
            System.exit(0);
        }
        
        while(true) {
            Socket clientSocket = null;
            boolean safe = false;
            
            try {
                clientSocket = serverSocket.accept();
                safe = true;
            }catch(IOException ioe) {
                ioe.printStackTrace();
                System.err.println("I/O error occured when waiting for a connection.");
            }catch(SecurityException se) {
                se.printStackTrace();
                System.err.println("Connection can not be accepted, permission denied.");
                System.exit(0);
            }
            if(safe) {
                threadList.add(new Thread(new FoxClientService(clientSocket)));
                threadList.get(threadList.size() - 1).start();
            }
        }
    }
}