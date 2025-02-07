package cz.meind.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * A class for logging messages to a file.
 */
public class Logger {

    private File logFile;

    /**
     * Constructs a Logger object and creates a log file at the specified path.
     *
     * @param path The path where the log file will be created.
     */
    public Logger(String path) {
        try {
            createLogFile(path);
        } catch (IOException e) {
            System.out.println(Logger.class + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] ERROR: " + e);
        }
    }

    /**
     * Returns the log file associated with this Logger object.
     *
     * @return The log file.
     */
    public File getLogFile() {
        return logFile;
    }

    /**
     * Creates a log file at the specified path.
     *
     * @param path The path where the log file will be created.
     * @throws IOException If an I/O error occurs while creating the log file.
     */
    private void createLogFile(String path) throws IOException {
        logFile = new File(path);
        if (!Files.exists(Path.of(logFile.getPath()))) {
            if (logFile.getParent() != null) Files.createDirectories(Path.of(logFile.getParent()));
            Files.createFile(Path.of(logFile.getPath()));
        }
        write("https://github.com/WMeindW \n\n\nDaniel Linda, cz.meind.PeerToPeerBank");
    }

    /**
     * Writes a message to the log file.
     *
     * @param content The message to be written to the log file.
     */
    private synchronized void write(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException ignore) {
        }
    }

    /**
     * Logs a message to the console and the log file.
     *
     * @param message The message to be logged.
     */
    public void message(String message) {
        System.out.println(message);
        write(message);
    }

    /**
     * Logs an error message to the console and the log file.
     *
     * @param c       The class where the error occurred.
     * @param message The error message.
     */
    public void error(Class<?> c, String message) {
        System.err.println(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] ERROR: " + message);
        write(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] ERROR: " + message);
    }

    /**
     * Logs an error message to the console and the log file, including the stack trace of the exception.
     *
     * @param c The class where the error occurred.
     * @param e The exception.
     */
    public void error(Class<?> c, Exception e) {
        System.err.println(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] ERROR: " + e.toString());
        write(c.getName() + " [" + LocalDateTime.now() + "] ERROR: " + e);
    }

    /**
     * Logs an informational message to the console and the log file.
     *
     * @param c       The class where the informational message occurred.
     * @param message The informational message.
     */
    public void info(Class<?> c, String message) {
        System.out.println(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] INFO: " + message);
        write(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] INFO: " + message);
    }

    /**
     * Logs a warning message to the console and the log file.
     *
     * @param c       The class where the warning occurred.
     * @param message The warning message.
     */
    public void warn(Class<?> c, String message) {
        System.out.println(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] WARN: " + message);
        write(c.getName() + " [" + LocalDateTime.now() + "] [" + Thread.currentThread() + "] WARN: " + message);
    }
}
