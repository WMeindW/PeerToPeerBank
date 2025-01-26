package cz.meind.command;

import cz.meind.application.Application;

public class BankCodeC implements Command {
    @Override
    public String execute(String[] args) {
        return args[0] + " " + Application.hostAddress;
    }
}
