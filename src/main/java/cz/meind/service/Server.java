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

    public Server() {
        populateCommands();
        executor = Executors.newFixedThreadPool(Application.poolSize);
        serverThread = new Thread(Listener::listen);
        serverThread.setName("server");
        serverThread.start();
    }

    public void handle(Socket clientSocket) {
        try {
            Handler h = new Handler(clientSocket);
            executor.execute(h);
            dispatched.put(h.getName(), h);
        } catch (IOException e) {
            Application.logger.error(Server.class, e);
        }
    }

    public HashMap<String, Handler> getHandlers() {
        return dispatched;
    }

    public void releaseHandler(String name) {
        dispatched.remove(name);
    }

    public Thread getServerThread() {
        return serverThread;
    }

    public Command getCommand(String command) {
        return commands.getOrDefault(command, new ErrorC());
    }

    private void populateCommands() {
        commands.put("BC", new BankCodeC());
        commands.put("AC", new AccountCreateC());
        commands.put("AD", new AccountDepositC());
        commands.put("AW", new AccountWithdrawC());
        commands.put("AB", new AccountBalanceC());
        commands.put("AR", new AccountRemoveC());

    }
}
