package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.client.Client;
import cz.meind.database.entities.Account;

import java.math.BigDecimal;
import java.util.Optional;

import static cz.meind.application.Application.mapper;

public class AccountDepositC implements Command {
    @Override
    public String execute(String[] args) {
        if (args.length != 3) return "ER Špatný počet argumentů";
        int accountNumber;
        BigDecimal balanceToAdd;
        String bankCode;
        try {
            accountNumber = Integer.parseInt(args[1].split("/")[0]);
            bankCode = args[1].split("/")[1];
            balanceToAdd = new BigDecimal(args[2]);
        } catch (Exception e) {
            return "ER Špatný formát argumentů";
        }

        Optional<Account> a = mapper.fetchAll(Account.class).stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst();
        if (a.isEmpty()) return "ER Účet s tímto číslem neexistuje";
        if (!bankCode.equals(Application.hostAddress)) {
            if (Client.scanHost(bankCode)) return "AD " + bankCode;
            return "ER Neznámá banka";
        }
        Account account = a.get();
        account.setBalance(account.getBalance().add(balanceToAdd));
        try {
            mapper.update(account);
        } catch (IllegalAccessException e) {
            return "ER Nastala chyba při ukládání dat";
        }
        return "AD";
    }
}
