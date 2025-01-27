package cz.meind.command;

import cz.meind.database.entities.Account;

import java.math.BigDecimal;

import static cz.meind.application.Application.mapper;

public class BankAmountC implements Command {
    @Override
    public String execute(String[] args) {
        BigDecimal total = new BigDecimal(0);
        for (Account account : mapper.fetchAll(Account.class))
            total = total.add(account.getBalance());
        return "BA " + total;
    }
}
