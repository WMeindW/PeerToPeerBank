package cz.meind.service.asynch;

import cz.meind.application.Application;
import cz.meind.service.Parser;


import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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
                    String[] args = Parser.parse(instruction);
                    if (args[0].equalsIgnoreCase("exit")) throw new IOException("Client requested close");
                    write(Application.server.getCommand(args[0]).execute(args));
                }
            }

        } catch (IOException e) {
            Application.logger.error(Handler.class, e);
        } finally {
            close();
        }
    }

    private void write(String message) {
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(message);
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
