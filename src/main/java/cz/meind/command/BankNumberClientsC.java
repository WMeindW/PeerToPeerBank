package cz.meind.command;

import cz.meind.database.entities.Account;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static cz.meind.application.Application.mapper;

public class BankNumberClientsC implements Command {
    @Nonnull
    @Override
    public String execute(String[] args) {
        int total = mapper.fetchAll(Account.class).size();
        return "BN " + total;
    }
}
