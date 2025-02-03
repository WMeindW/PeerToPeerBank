package cz.meind.application;

import cz.meind.client.Client;
import cz.meind.database.DatabaseContext;
import cz.meind.logger.Logger;
import cz.meind.mapper.ObjectMapper;
import cz.meind.service.Server;
import cz.meind.service.asynch.Daemon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Application {
    public static Logger logger;

    public static Server server;

    public static String configFilePath = "src/main/resources/application.properties";

    public static String logFilePath = "/home/ubuntu/mandis/log/log.txt";

    public static int port = 8088;

    public static int poolSize = 16;

    public static String dbUrl = "jdbc:mysql://localhost:3306/andrem";

    public static String dbUser = "root";

    public static String dbPassword = "password";

    public static ObjectMapper mapper;

    public static DatabaseContext database;

    public static String hostAddress = findLocalInetAddress();

    public static int kickTimeout = 15;

    public static int scanTimeout = 100;

    public static int connectTimeout = 2500;

    public static int readTimeout = 2500;

    public static int scanThreadCount = 5;

    public static int taskTimeout = 5000;

    public static Client client;

    /**
     * Initializes and starts the application components including the logger, configuration,
     * daemon thread, and server.
     *
     * @param args Command-line arguments where the first argument can specify the path
     *             to the configuration file. If provided, it overrides the default config file path.
     */
    public static void run(String[] args) {
        initializeConfig(args);
        initializeLogger();
        initializeDatabaseProfile();
        initializeServer();
        initializeDaemon();
        Application.logger.info(Application.class,"Start complete");
    }

    /**
     * Initializes the daemon thread for the application.
     * This method sets up a shutdown hook to ensure the daemon
     * is properly shut down when the application exits. It then
     * creates and starts a new daemon thread.
     */
    private static void initializeDaemon() {
        Runtime.getRuntime().addShutdownHook(new Thread(Daemon::shutdown));
        Application.logger.info(Daemon.class, "Starting daemon.");
        Thread t = new Thread(new Daemon());
        t.setDaemon(true);
        t.start();
    }

    /**
     * Initializes the server component of the application.
     * This method logs the start of the server initialization process
     * and creates a new instance of the Server class.
     */
    private static void initializeServer() {
        client = new Client();
        Application.logger.info(Server.class, "Starting server.");
        server = new Server();
    }

    /**
     * Initializes the logger for the application.
     * This method sets up the logger with the specified log file path
     * and logs initial messages including a reference URL and a startup message.
     */
    private static void initializeLogger() {
        logger = new Logger(logFilePath);
        logger.info(Application.class, "Starting application.");
    }

    private static void initializeDatabaseProfile() {
        Application.logger.info(Application.class, "Initializing database profile.");
        database = new DatabaseContext();
        Application.logger.info(Application.class, "Creating connection.");
        mapper = new ObjectMapper(Application.database.getConnection());
    }

    /**
     * Initializes the application configuration based on the provided arguments.
     * If no arguments are provided, the default configuration file path is used.
     * The function reads the configuration properties from the file and sets the
     * corresponding application variables.
     *
     * @param args Command-line arguments where the first argument can specify the path
     *             to the configuration file. If provided, it overrides the default config file path.
     */
    private static void initializeConfig(String[] args) {
        System.out.println("https://github.com/WMeindW \n\n\nDaniel Linda, cz.meind.PeerToPeerBank");
        if (args.length > 0 && args[0] != null) configFilePath = args[0];
        Properties properties = new Properties();
        File configFile = new File(configFilePath);
        if (!configFile.exists()) return;
        try {
            properties.load(new FileInputStream(configFilePath));
        } catch (IOException e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
            return;
        }
        System.out.println(Application.class.getName() + " [" + LocalDateTime.now() + "] INFO: " + "Found config at " + configFilePath);
        try {
            taskTimeout = Integer.parseInt(properties.getProperty("handler.task.timeout"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            readTimeout = Integer.parseInt(properties.getProperty("server.client.read.timeout"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            connectTimeout = Integer.parseInt(properties.getProperty("server.client.connect.timeout"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            scanThreadCount = Integer.parseInt(properties.getProperty("server.client.scan.threads"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            scanTimeout = Integer.parseInt(properties.getProperty("server.client.scan.timeout"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            kickTimeout = Integer.parseInt(properties.getProperty("server.client.timeout"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            dbUrl = properties.getProperty("database.url");
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            dbUser = properties.getProperty("database.user");
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            dbPassword = properties.getProperty("database.password");
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            if (properties.getProperty("log.file.path") != null) logFilePath = properties.getProperty("log.file.path");
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            if (properties.getProperty("server.host.address") != null)
                hostAddress = properties.getProperty("server.host.address");
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            port = Integer.parseInt(properties.getProperty("server.port"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }
        try {
            poolSize = Integer.parseInt(properties.getProperty("server.thread.pool.size"));
        } catch (Exception e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
        }

        System.out.println(Application.class.getName() + " [" + LocalDateTime.now() + "] INFO: " + properties);
    }

    private static String findLocalInetAddress() {
        try {
            Optional<InetAddress> ia = Arrays.stream(InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())).filter(InetAddress::isSiteLocalAddress).findFirst();
            if (ia.isPresent()) return ia.get().toString().split("/")[1];
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: Could not resolve local host, defaulting to default or config address");
            return null;
        } catch (UnknownHostException e) {
            System.err.println(Application.class.getName() + " [" + LocalDateTime.now() + "] ERROR: Could not resolve local host, defaulting to default or config address");
            return null;
        }
    }
}
