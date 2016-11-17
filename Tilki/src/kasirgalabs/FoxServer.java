package kasirgalabs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoxServer class implements an application that accepts connection from
 * clients and receives data. This class uses {@link java.net.ServerSocket}
 * class to accept incoming connections. Every incoming connection will be
 * handled by a thread. The maximum queue length for incoming connection is 100.
 * If a connection arrives when the queue is full, the connection is refused.
 */
public class FoxServer {
    private static ExecutorService executor = null;
    private static ServerSocket serverSocket = null;
    private static ArrayList<Future<Integer>> futureList = null;
    private static volatile boolean serverDown = false;

    /**
     * @param args Not used.
     */
    public static void main(String[] args) {
        ShutDownHook hook = new ShutDownHook();
        Runtime.getRuntime().addShutdownHook(hook);
        try {
            serverSocket = new ServerSocket(50101, 200);
        }catch(IOException ex) {
            Logger.getLogger(FoxServer.class.getName()).log(Level.SEVERE, null,
                                                            ex);
            Runtime.getRuntime().removeShutdownHook(hook);
            System.exit(0);
        }
        futureList = new ArrayList<Future<Integer>>();
        executor = Executors.newFixedThreadPool(200);

        //FoxServerSetup.main(null);
        Socket clientSocket = null;
        while(true) {
            try {
                clientSocket = serverSocket.accept();
            }catch(IOException ex) {
                if(!serverDown) {
                    Logger.getLogger(FoxServer.class.getName()).
                            log(Level.SEVERE, null, ex);
                    System.exit(0);
                }
            }
            try {
                futureList.add(executor.submit(
                        new FoxServiceThread(clientSocket)));
            }catch(IOException ex) {
                Logger.getLogger(FoxServer.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    private static class ShutDownHook extends Thread {
        @Override
        public void run() {
            System.err.println("Running shut down hook...");
            serverDown = true;
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
                System.out.println("Successful termination.");
            }catch(InterruptedException ex) {
                Logger.getLogger(FoxServer.class.getName()).log(Level.SEVERE,
                                                                null, ex);
                System.out.println("Unsuccessful termination.");
                executor.shutdownNow();
            }finally {
                try {
                    serverSocket.close();
                }catch(IOException ex) {
                    Logger.getLogger(FoxServer.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
            Future<Integer> future;
            Iterator< Future< Integer>> iterator = futureList.iterator();
            System.err.println("Thread statuses:");
            while(iterator.hasNext()) {
                future = iterator.next();
                try {
                    System.err.println(future.get(100, TimeUnit.MILLISECONDS));
                }catch(InterruptedException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.SEVERE, null, ex);
                }catch(ExecutionException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.SEVERE, null, ex);
                }catch(TimeoutException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
    }
}
