package cz.meind.service.asynch;

import cz.meind.application.Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * This class represents a socket server listener for an asynchronous web server.
 * It provides methods to start and listen for incoming client connections.
 */
public class Listener {
    private static ServerSocket server;

    /**
     * Returns the server socket instance.
     *
     * @return the server socket
     */
    public static ServerSocket getServer() {
        return server;
    }

    /**
     * Starts the server socket on the specified port.
     * Logs a message indicating the successful start.
     * Throws a RuntimeException if an I/O error occurs.
     */
    private static void start() throws IOException {
        server = new ServerSocket(Application.port, 50, InetAddress.getByName(Application.hostAddress));
        Application.logger.info(Listener.class, "Socket server started on port " + Application.port + " and address " + Application.hostAddress);
    }

    /**
     * Runs the server socket listener.
     * Accepts incoming client connections and delegates handling to the server's handler.
     */
    private static void run() {
        try {
            start();
            while (true) {
                Socket clientSocket = server.accept();
                Application.server.handle(clientSocket);
                Application.logger.info(Listener.class, "Accepted client socket from " + clientSocket.getInetAddress().toString().replace("/",""));
            }
        } catch (IOException e) {
            Application.logger.error(Listener.class, "Fatal server error: " + e);
        }

    }

    /**
     * Listens for incoming client connections.
     * Waits for 1 second before starting the server.
     * Logs any I/O or interruption errors and exits the program with status code 130 if interrupted.
     */
    public static void listen() {
        try {
            Thread.sleep(1000);
            run();
        } catch (InterruptedException e) {
            Application.logger.error(Listener.class, e);
            System.exit(130);
        }
    }
}

