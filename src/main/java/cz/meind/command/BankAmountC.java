package cz.meind.command;

import cz.meind.database.entities.Account;

import java.math.BigDecimal;

import static cz.meind.application.Application.mapper;

public class BankAmountC implements Command {
    @Override
    public String execute(String[] args) {
        Long total = 0L;
        for (Account account : mapper.fetchAll(Account.class))
            total += account.getBalance();
        return "BA " + total;
    }
}
