package cz.meind.service;

import cz.meind.application.Application;
import cz.meind.command.*;
import cz.meind.service.asynch.Handler;
import cz.meind.service.asynch.Listener;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService executor;

    private final Thread serverThread;

    private final HashMap<String, Command> commands = new HashMap<>();

    private final HashMap<String, Handler> dispatched = new HashMap<>();

    /**
     * Initializes a new instance of the Server class.
     * This constructor sets up the server by populating commands, creating a new fixed thread pool,
     * starting a new thread for listening to incoming connections, and naming it "listener".
     */
    public Server() {
        populateCommands();
        executor = Executors.newFixedThreadPool(Application.poolSize);
        serverThread = new Thread(Listener::listen);
        serverThread.setName("listener");
        serverThread.start();
    }

    /**
     * Handles incoming client connections by creating a new Handler for each connection.
     * The Handler is then executed in the executor service and added to the dispatched map.
     * If an IOException occurs during the handling process, it is logged using the logger from the Application class.
     *
     * @param clientSocket The socket representing the client connection.
     */
    public void handle(Socket clientSocket) {
        try {
            Handler h = new Handler(clientSocket);
            executor.execute(h);
            dispatched.put(h.getName(), h);
        } catch (IOException e) {
            Application.logger.error(Server.class, e);
        }
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdownExecutor() {
        executor.shutdownNow();
    }

    /**
     * Returns the map of currently dispatched handlers.
     *
     * @return The map of dispatched handlers.
     */
    public HashMap<String, Handler> getHandlers() {
        return dispatched;
    }

    /**
     * Removes a handler from the dispatched map by its name.
     *
     * @param name The name of the handler to be removed.
     */
    public void releaseHandler(String name) {
        dispatched.remove(name);
    }

    /**
     * Returns the server thread.
     *
     * @return The server thread.
     */
    public Thread getServerThread() {
        return serverThread;
    }

    /**
     * Returns a command instance based on the given command code.
     * If the command code is not found, returns an instance of ErrorC.
     *
     * @param command The command code.
     * @return The corresponding command instance.
     */
    public Command getCommand(String command) {
        return commands.getOrDefault(command, new ErrorC());
    }

    /**
     * Populates the commands HashMap with command codes and their corresponding Command instances.
     * This method is called during the initialization of the Server class.
     *
     * @throws NoClassDefFoundError If any of the Command classes (BankCodeC, AccountCreateC, etc.) are not found.
     */
    private void populateCommands() {
        commands.put("BC", new BankCodeC());
        commands.put("AC", new AccountCreateC());
        commands.put("AD", new AccountDepositC());
        commands.put("AW", new AccountWithdrawC());
        commands.put("AB", new AccountBalanceC());
        commands.put("AR", new AccountRemoveC());
        commands.put("BA", new BankAmountC());
        commands.put("BN", new BankNumberClientsC());
        commands.put("RP", new RobberyPlanC());
    }
}
