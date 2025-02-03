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
        if (!bankCode.equals(Application.hostAddress))
            return Application.client.command(bankCode, args[0] + " " + args[1]);
        Optional<Account> a = mapper.fetchAll(Account.class).stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst();
        return a.map(account -> "AB " + account.getBalance()).orElse("ER Účet s tímto číslem neexistuje");
    }
}
