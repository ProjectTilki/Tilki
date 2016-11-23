package kasirgalabs;

import java.io.IOException;
import static java.lang.System.setProperty;
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
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
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
    private static volatile boolean serverDown = false;
    private static volatile boolean executorDown = false;
    private static final Logger LOGGER;
    private static final FoxServerLogManager LOG_MANAGER;
    static {
        // must be called before any Logger method is used.
        setProperty("java.util.logging.manager", FoxServerLogManager.class.getName());
        LOG_MANAGER = (FoxServerLogManager) LogManager.getLogManager();
        LOGGER = Logger.getLogger(FoxServer.class.getName());
        
        try {
            FileHandler fh = new FileHandler("server.log");
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(ch);
        }catch(SecurityException e) {
            e.printStackTrace();
            System.exit(0);
        }catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * @param args Not used.
     */
    public static void main(String[] args) {
        ShutDownHook hook = new ShutDownHook();
        Runtime.getRuntime().addShutdownHook(hook);
        try {
            serverSocket = new ServerSocket(50101, 200);
        }catch(IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
                    LOGGER.log(Level.SEVERE, null, ex);
                    System.exit(0);
                }
            }
            try {
                if(serverDown || executorDown)
                    System.exit(0);
                futureList.addLast(executor.submit(new FoxServiceThread(clientSocket)));
            }catch(RejectedExecutionException ex) {
                if(serverDown || executorDown)
                    System.exit(0);
                LOGGER.log(Level.SEVERE, "Can not create a thread for connection.", ex);
            }catch(IOException ex) {
                LOGGER.log(Level.FINE, "Client connection stream problem.", ex);
            }catch(NullPointerException ex) {
                LOGGER.log(Level.INFO, "Server might be received shut down operation.", ex);
            }
        }
    }

    private static class ShutDownHook extends Thread {
        private static final Logger asd = Logger.getLogger(ShutDownHook.class.getName());
        
        @Override
        public void run() {
            LOGGER.info("Running shut down hook...");
            serverDown = true;
            executorDown = true;
            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.SECONDS);
                LOGGER.info("No running process.");
            }catch(InterruptedException ex) {
                LOGGER.log(Level.WARNING, "Some processes may not be completed.", ex);
            }finally {
                try {
                    serverSocket.close();
                }catch(IOException ex) {
                    LOGGER.log(Level.FINE, null, ex);
                }
            }
            Future<Integer> future;
            Iterator< Future< Integer>> iterator = futureList.iterator();
            while(iterator.hasNext()) {
                future = iterator.next();
                try {
                    future.get(100, TimeUnit.MILLISECONDS);
                }catch(InterruptedException ex) {
                    LOGGER.log(Level.FINE, null, ex);
                }catch(ExecutionException ex) {
                    LOGGER.log(Level.SEVERE, "A session failed with an exception.", ex);
                }catch(TimeoutException ex) {
                    LOGGER.log(Level.WARNING, "Shuting down without waiting for uncompleted tasks.", ex);
                }
            }
            LOGGER.info("Shutting down is completed.");
            LOG_MANAGER.enableReset();
            LOG_MANAGER.reset();
        }
    }
}
