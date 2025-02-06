package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.database.entities.Account;

import javax.annotation.Nonnull;
import java.util.Optional;

import static cz.meind.application.Application.mapper;

public class AccountWithdrawC implements Command {
    @Nonnull
    @Override
    public String execute(String[] args) {
        if (args.length != 3) return "ER Špatný počet argumentů";
        int accountNumber;
        Long balanceToSubtract;
        String bankCode;
        try {
            accountNumber = Integer.parseInt(args[1].split("/")[0]);
            bankCode = args[1].split("/")[1];
            balanceToSubtract = Long.valueOf(args[2]);
        } catch (Exception e) {
            return "ER Špatný formát argumentů";
        }
        if (!bankCode.equals(Application.hostAddress))
            return Application.client.command(bankCode, args[0] + " " + args[1] + " " + args[2]);
        Optional<Account> a = mapper.fetchAll(Account.class).stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst();
        if (a.isEmpty()) return "ER Účet s tímto číslem neexistuje";

        Account account = a.get();
        if (balanceToSubtract.compareTo(account.getBalance()) > 0) return "ER Nedostatek finančních prostředků";
        account.setBalance(account.getBalance() - balanceToSubtract);
        try {
            mapper.update(account);
        } catch (IllegalAccessException e) {
            return "ER Nastala chyba při ukládání dat";
        }
        return "AW";
    }
}
