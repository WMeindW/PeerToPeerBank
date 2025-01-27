package cz.meind.client;

import cz.meind.application.Application;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class Client {
    private static boolean testAlive(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static int scanHost(String hostIp) {
        for (int i = 65525; i <= 65535; i++) {
            if (testAlive(hostIp, i, Application.scanTimeout)) {
                Application.logger.info(Client.class, "Found bank at: " + i);
                return i;
            }
        }
        throw new RuntimeException();
    }

    private static void write(String command, PrintWriter writer) {
        writer.println(command);
    }

    private static String read(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    public static String execute(String ip, String command) {
        int port;
        try {
            port = scanHost(ip);
        } catch (RuntimeException e) {
            return "ER Banka nenalezena";
        }
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress, Application.connectTimeout);
            socket.setSoTimeout(Application.readTimeout);
            write(command, new PrintWriter(socket.getOutputStream(), true));
            return read(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        }catch (SocketTimeoutException e) {
            return "ER Odpověď banky trvala příliš dlouho";
        } catch (IOException e) {
            Application.logger.error(Client.class, e);
            return "ER Nastala chyba při připojování k bance";
        }
    }
}
