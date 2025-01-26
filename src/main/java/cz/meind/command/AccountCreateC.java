package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.database.entities.Account;

import java.util.ArrayList;
import java.util.Collections;

import static cz.meind.application.Application.mapper;

public class AccountCreateC implements Command {
    @Override
    public String execute(String[] args) {
        ArrayList<Account> al = (ArrayList<Account>) mapper.fetchAll(Account.class);
        if (!al.isEmpty()) {
            Collections.sort(al);
            if (al.get(al.size() - 1).getAccountNumber() >= 99999)
                return "ER Banka v tuto chvíli neumožňuje založení nového účtu";
            Account a = new Account(al.get(al.size() - 1).getAccountNumber() + 1);
            mapper.save(a);
            return args[0] + " " + a.getAccountNumber() + "/" + Application.hostAddress;
        }
        Account a = new Account(10000);
        mapper.save(a);
        return args[0] + " " + a.getAccountNumber() + "/" + Application.hostAddress;
    }
}
