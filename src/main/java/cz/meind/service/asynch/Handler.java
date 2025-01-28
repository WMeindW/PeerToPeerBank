package cz.meind.service.asynch;

import cz.meind.application.Application;
import cz.meind.service.Parser;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * This class represents a handler for incoming client connections.
 * It manages the processing of requests and responses for each client.
 */
public class Handler implements Runnable {

    private final String name;

    private Socket client;

    private final InputStream in;

    private final OutputStream out;

    private int timestamp;

    private boolean closed;

    private final Thread currentThread;

    /**
     * Constructs a new Handler with the given socket object.
     *
     * @param client Client socket object.
     */
    public Handler(Socket client) throws IOException {
        currentThread = Thread.currentThread();
        this.name = currentThread.getName();
        this.client = client;
        this.in = client.getInputStream();
        this.out = client.getOutputStream();
        closed = false;
    }

    /**
     * Returns the unique identifier of the handler.
     *
     * @return The id of the handler.
     */
    public String getName() {
        return name;
    }

    public void incrementTimestamp() {
        timestamp++;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String instruction;
        try {
            while (!closed && (instruction = reader.readLine()) != null) {
                timestamp = 0;
                if (!instruction.isEmpty()) {
                    String[] args = Parser.parse(instruction);
                    if (args[0].equalsIgnoreCase("exit")) throw new IOException("Client requested close");
                    write(executeWithTimeout(() -> Application.server.getCommand(args[0]).execute(args), Application.taskTimeout));
                }
            }

        } catch (SocketException e) {
            Application.logger.error(Handler.class, "Closing socket");
        } catch (IOException e) {
            Application.logger.error(Handler.class, e);
        } finally {
            if (!closed) close();
        }
    }

    private void write(String message) {
        if (closed) return;
        try {
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(message);
        } catch (Exception e) {
            Application.logger.error(Handler.class,"Could not write " + message + " to client socket");
        }

    }

    private String executeWithTimeout(Callable<String> task, int timeoutInMilliseconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);

        try {
            return future.get(timeoutInMilliseconds, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return "ER Úkol trval příliš dlouho";
        } catch (Exception e) {
            Application.logger.error(Handler.class, e);
            return "ER Nastala neznámá chyba - zkontrolujte formát příkazu";
        } finally {
            Application.logger.info(Handler.class, "Task finished");
            future.cancel(true); // Cancel the task if still running
            executor.shutdownNow(); // Shut down the executor
        }
    }

    /**
     * Closes the client connection and releases the handler.
     */
    public void close() {
        closed = true;
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            Application.logger.error(Handler.class, e);
        }
        client = null;
        Application.server.releaseHandler(this.name);
        Application.logger.info(Handler.class, "Client disconnected");
        currentThread.interrupt();
    }
}
