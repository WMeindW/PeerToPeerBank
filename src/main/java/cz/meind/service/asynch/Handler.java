package cz.meind.service.asynch;

import cz.meind.application.Application;


import java.net.Socket;

/**
 * This class represents a handler for incoming client connections.
 * It manages the processing of requests and responses for each client.
 */
public class Handler {

    private final int id;

    private Socket client;

    /**
     * Constructs a new Handler with the given id.
     *
     * @param id The unique identifier for the handler.
     */
    public Handler(int id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of the handler.
     *
     * @return The id of the handler.
     */
    public int getId() {
        return id;
    }

    /**
     * Handles an incoming client connection by creating a new thread to process the request.
     *
     * @param c The client socket.
     */
    public void handle(Socket c) {
        Thread thread = new Thread(this::run);
        client = c;
        Application.logger.info(Handler.class, "Dispatching thread: " + thread.getClass() + " with id " + id + " and priority " + thread.getPriority());
        thread.start();
    }
    private void run() {
        close();
    }

    /**
     * Closes the client connection and releases the handler.
     */
    private void close() {
        client = null;
        Thread.currentThread().interrupt();
    }

}
