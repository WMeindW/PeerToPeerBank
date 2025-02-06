package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.client.Client;

import javax.annotation.Nonnull;

public class BankCodeC implements Command {
    @Nonnull
    @Override
    public String execute(String[] args) {
        return args[0] + " " + Application.hostAddress;
    }
}
