package kasirgalabs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * FoxServer class implements an application that accepts connection from
 * clients and receives data. This class uses {@link java.net.ServerSocket}
 * class to accept incoming connections. Every incoming connection will be
 * handled by a thread. The maximum queue length for incoming connection is 200.
 * If a connection arrives when the queue is full, the connection is refused.
 */
public class FoxServer {
    private static ExecutorService executor;
    private static ServerSocket serverSocket;
    private static ConcurrentLinkedDeque<Future<Integer>> futureList;
    private static boolean serverDown = false;
    private static boolean executorDown = false;

    /**
     * @param args Not used.
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(FoxServer.class.getName());
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("server.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } 
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
        futureList = new ConcurrentLinkedDeque<Future<Integer>>();
        executor = Executors.newFixedThreadPool(200);

        //FoxServerSetup.main(null);
        Socket clientSocket = null;
        while(true && !Thread.currentThread().isInterrupted()) {
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
                futureList.addLast(executor.submit(new FoxServiceThread(
                        clientSocket)));
            }catch(RejectedExecutionException ex) {
                if(!serverDown || !executorDown)
                    Logger.getLogger(FoxServer.class.getName()).
                            log(Level.SEVERE,
                                "Can not create a thread for connection.", ex);
            }catch(IOException ex) {
                Logger.getLogger(FoxServer.class.getName()).
                        log(Level.FINE, "Client connection stream problem.",
                            ex);
            }
        }
    }

    private static class ShutDownHook extends Thread {
        @Override
        public void run() {
            Logger.getLogger(FoxServer.class.getName()).log(Level.INFO,
                                                            "Running shut down hook...");
            serverDown = true;
            executorDown = true;
            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.SECONDS);
                Logger.getLogger(FoxServer.class.getName()).log(Level.INFO,
                                                                "No running process.");
            }catch(InterruptedException ex) {
                Logger.getLogger(FoxServer.class.getName()).log(Level.WARNING,
                                                                "Some processes may not be completed.",
                                                                ex);
            }finally {
                try {
                    serverSocket.close();
                }catch(IOException ex) {
                    Logger.getLogger(FoxServer.class.getName()).
                            log(Level.FINE, null, ex);
                }
            }
            Future<Integer> future;
            Iterator< Future< Integer>> iterator = futureList.iterator();
            Logger.getLogger(FoxServer.class.getName()).log(Level.INFO,
                                                            "Thread statuses:");
            while(iterator.hasNext()) {
                future = iterator.next();
                try {
                    future.get(100, TimeUnit.MILLISECONDS);
                }catch(InterruptedException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.INFO, null, ex);
                }catch(ExecutionException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.SEVERE,
                            "A session failed with an exception.", ex);
                }catch(TimeoutException ex) {
                    Logger.getLogger(FoxServer.class.getName()).log(
                            Level.WARNING,
                            "Shuting down without waiting for uncompleted tasks.",
                            ex);
                }
            }
            Logger.getLogger(FoxServer.class.getName()).log(Level.INFO,
                                                            "Shuting down is completed.");
        }
    }
}
