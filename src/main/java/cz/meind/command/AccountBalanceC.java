package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.database.entities.Account;

import java.util.Optional;

import static cz.meind.application.Application.mapper;

public class AccountBalanceC implements Command {
    @Override
    public String execute(String[] args) {
        if (args.length != 2) return "ER Špatný počet argumentů";
        int accountNumber;
        String bankCode;
        try {
            accountNumber = Integer.parseInt(args[1].split("/")[0]);
            bankCode = args[1].split("/")[1];
        } catch (Exception e) {
            return "ER Špatný formát argumentů";
        }

        Optional<Account> a = mapper.fetchAll(Account.class).stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst();
        if (a.isEmpty()) return "ER Účet s tímto číslem neexistuje";
        if (!bankCode.equals(Application.hostAddress)) return "ER Špatná banka";
        return "AB " + a.get().getBalance();
    }
}
