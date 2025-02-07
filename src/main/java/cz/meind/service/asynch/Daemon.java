package cz.meind.service.asynch;

import cz.meind.application.Application;

import java.util.HashMap;
import java.util.Map;

public class Daemon {

    /**
     * This method starts the daemon that continuously monitors the application.
     * The daemon runs in a separate thread and checks the status of each client handler.
     * If a client handler has been inactive for more than the specified timeout, the client is considered timed-out and the handler is closed.
     *
     */
    public static void run() {
        Application.logger.info(Daemon.class, "Monitoring started");
        try {
            while (true) {
                Thread.sleep(1000);
                for (Map.Entry<String, Handler> entry : Application.server.getHandlers().entrySet()) {
                    Handler h = entry.getValue();
                    h.incrementTimestamp();
                    if (h.getTimestamp() > Application.kickTimeout) {
                        Application.logger.info(Daemon.class, "Client timed-out");
                        h.close();
                    }
                }
            }
        } catch (InterruptedException e) {
            Application.logger.error(Daemon.class, e);
            System.exit(110);
        }
    }

    /**
     * This method is used to gracefully shut down the daemon.
     * It interrupts the server thread and waits for a second to allow the server to stop properly.
     * If the server does not stop within the given time, it logs an error message and exits the application.
     */
    public static void shutdown() {
        Application.logger.info(Daemon.class, "Shutting down");
        Application.server.shutdownExecutor();
        Application.server.getServerThread().interrupt();
        Application.logger.info(Daemon.class, "Interrupted server");
        Application.logger.info(Daemon.class, "Closing database connection");
        Application.database.closeConnection();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Application.logger.info(Daemon.class, "Proper shutdown failed");
            System.exit(1);
        }
        Application.logger.info(Daemon.class, "Shutdown completed");
    }
}
