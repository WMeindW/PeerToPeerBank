package cz.meind.service.asynch;

import cz.meind.application.Application;


import java.io.*;
import java.net.Socket;

/**
 * This class represents a handler for incoming client connections.
 * It manages the processing of requests and responses for each client.
 */
public class Handler implements Runnable {

    private final String name;

    private Socket client;

    private final InputStream in;

    private final OutputStream out;

    /**
     * Constructs a new Handler with the given id.
     *
     * @param id The unique identifier for the handler.
     */
    public Handler(String id, Socket client) throws IOException {
        this.name = id;
        this.client = client;
        this.in = client.getInputStream();
        this.out = client.getOutputStream();
    }

    /**
     * Returns the unique identifier of the handler.
     *
     * @return The id of the handler.
     */
    public String getName() {
        return name;
    }


    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String instruction;
        try {
            while ((instruction = reader.readLine()) != null) {
                if (!instruction.isEmpty()) {
                    System.out.println(instruction);
                }
            }

        } catch (IOException e) {
            Application.logger.error(Handler.class, e);
        } finally {
            close();
        }
    }

    /**
     * Closes the client connection and releases the handler.
     */
    private void close() {
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            Application.logger.error(Handler.class, e);
        }
        client = null;
        Thread.currentThread().interrupt();
        Application.logger.info(Handler.class, "Client disconnected");
    }

}
