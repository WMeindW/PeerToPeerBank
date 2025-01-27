package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.client.Client;

public class BankCodeC implements Command {
    @Override
    public String execute(String[] args) {
        Client c = new Client();
        return args[0] + " " + Application.hostAddress;
    }
}
