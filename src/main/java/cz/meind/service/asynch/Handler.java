package cz.meind.service.asynch;

import cz.meind.application.Application;
import cz.meind.service.Parser;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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

    /**
     * This method is responsible for handling incoming client connections and processing their requests.
     * It reads instructions from the client, executes them, and sends responses back.
     * The method runs in an infinite loop until the client disconnects or an error occurs.
     * If the client sends an "exit" command, an IOException is thrown to close the connection.
     * The method also manages the timestamp and closes the client connection when necessary.
     */
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

    /**
     * Writes a message to the client socket.
     *
     * @param message The message to be sent to the client.
     *                <p>
     *                This method checks if the client connection is closed. If not, it attempts to write the given message to the client socket.
     *                If an exception occurs during the writing process, an error message is logged using the application's logger.
     * @return void
     */
    private void write(String message) {
        if (closed) return;
        try {
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(message);
        } catch (Exception e) {
            Application.logger.error(Handler.class, "Could not write " + message + " to client socket");
        }
    }

    /**
     * Executes a given task with a specified timeout.
     *
     * @param task                  The task to be executed. It should be a Callable that returns a String.
     * @param timeoutInMilliseconds The maximum time to wait for the task to complete.
     * @return The result of the task if it completes within the timeout.
     * If the task does not complete within the timeout, it returns a predefined error message.
     * If an exception occurs during the execution of the task, it logs the error and returns a predefined error message.
     */
    private String executeWithTimeout(Callable<String> task, int timeoutInMilliseconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);
        String value = "ER";
        try {
            return value = future.get(timeoutInMilliseconds, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return value = "ER Úkol trval příliš dlouho";
        } catch (Exception e) {
            Application.logger.error(Handler.class, e);
            return value = "ER Nastala neznámá chyba - zkontrolujte formát příkazu";
        } finally {
            Application.logger.info(Handler.class, "Task with response \"" + value + "\" finished");
            future.cancel(true);
            executor.shutdownNow();
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
