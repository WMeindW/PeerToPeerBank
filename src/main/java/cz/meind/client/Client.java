package cz.meind.client;

import cz.meind.application.Application;
import cz.meind.service.Parser;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private boolean testAlive(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int scanHost(String hostIp) {
        for (int i = 65525; i <= 65535; i++) {
            if (testAlive(hostIp, i, Application.scanTimeout)) {
                Application.logger.info(Client.class, "Found bank at: " + hostIp + "/" + i);
                return i;
            }
        }
        throw new RuntimeException();
    }

    private void write(String command, PrintWriter writer) {
        writer.println(command);
    }

    private String read(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private HashMap<String, Integer> scanNetwork() throws InterruptedException {
        ConcurrentHashMap<String, Integer> banks = new ConcurrentHashMap<>();
        int interfaceID = Integer.parseInt(Application.hostAddress.split("\\.")[3]);
        String subnet = Application.hostAddress.substring(0, Application.hostAddress.length() - Integer.toString(interfaceID).length());
        ExecutorService executor = Executors.newFixedThreadPool(Application.scanThreadCount);
        AtomicInteger scanned = new AtomicInteger();
        AtomicInteger found = new AtomicInteger();
        Collection<Callable<String>> tasks = new LinkedList<>();
        for (int i = 1; i <= 254; i++) {
            if (i == interfaceID) continue;
            int finalI = i;
            tasks.add(() -> {
                try {
                    scanned.getAndIncrement();
                    int port = scanHost(subnet + finalI);
                    banks.put(subnet + finalI, port);
                    found.getAndIncrement();
                } catch (RuntimeException ignore) {
                }
                return null;
            });
        }
        executor.invokeAll(tasks);
        executor.shutdownNow();
        Application.logger.info(Client.class, "Scanned " + scanned + " ips and found " + found + " banks");
        return new HashMap<>(banks);
    }

    public LinkedList<Bank> analyzeBanks() throws InterruptedException {
        long start = System.currentTimeMillis();
        LinkedList<Bank> analyzedBanks = new LinkedList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Application.scanThreadCount);
        Collection<Callable<String>> tasks = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : scanNetwork().entrySet()) {
            tasks.add(() -> {
                try {
                    BigInteger total = new BigInteger(Parser.parse(execute(entry.getKey(), entry.getValue(), "BA"))[1]);
                    Integer number = Integer.valueOf(Parser.parse(execute(entry.getKey(), entry.getValue(), "BN"))[1]);
                    analyzedBanks.add(new Bank(entry.getKey(), total, number));
                } catch (Exception ignored) {
                }
                return null;
            });
        }
        executor.invokeAll(tasks);
        executor.shutdownNow();
        Application.logger.info(Client.class, "Task took: " + (System.currentTimeMillis() - start) + " ms");
        return analyzedBanks;
    }

    public String command(String ip, String command) {
        int port;
        try {
            port = scanHost(ip);
        } catch (RuntimeException e) {
            return "ER Banka nenalezena";
        }
        return execute(ip, port, command);
    }

    private String execute(String ip, int port, String command) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress, Application.connectTimeout);
            socket.setSoTimeout(Application.readTimeout);
            write(command, new PrintWriter(socket.getOutputStream(), true));
            return read(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        } catch (SocketTimeoutException e) {
            return "ER Odpověď banky trvala příliš dlouho";
        } catch (IOException e) {
            Application.logger.error(Client.class, e);
            return "ER Nastala chyba při připojování k bance";
        }
    }
}
