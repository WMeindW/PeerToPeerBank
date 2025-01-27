package cz.meind.client;

import cz.meind.application.Application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    public static boolean scanHost(String hostIp) {
        for (int i = 65525; i <= 65535 ; i++) {
            if (testAlive(hostIp,i, Application.scanTimeout)){
                System.out.println("Found host at: " + i);
                return true;
            }
        }
        return false;
    }
}
