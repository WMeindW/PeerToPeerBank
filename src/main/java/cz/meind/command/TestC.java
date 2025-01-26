package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.database.entities.Account;

public class TestC implements Command {
    @Override
    public String execute(String[] args) {
        return Application.mapper.fetchById(Account.class, Integer.parseInt(args[1])) + " test";
    }
}
