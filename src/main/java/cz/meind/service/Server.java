package cz.meind.service;

import cz.meind.application.Application;
import cz.meind.command.Command;
import cz.meind.command.ErrorCommand;
import cz.meind.command.TestCommand;
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

    public Server() {
        populateCommands();
        executor = Executors.newFixedThreadPool(Application.poolSize);
        serverThread = new Thread(Listener::listen);
        serverThread.setName("server");
        serverThread.start();
    }

    public void handle(Socket clientSocket) {
        try {
            executor.execute(new Handler(Thread.currentThread().getName(), clientSocket));
        } catch (IOException e) {
            Application.logger.error(Server.class, e);
        }
    }

    public Thread getServerThread() {
        return serverThread;
    }

    public Command getCommand(String command) {
        return commands.getOrDefault(command, new ErrorCommand());
    }

    private void populateCommands() {
        commands.put("T", new TestCommand());
    }

    public void stop() {
        serverThread.interrupt();
    }
}
