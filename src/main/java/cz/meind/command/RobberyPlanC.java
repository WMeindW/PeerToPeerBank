package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.client.Bank;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;

public class RobberyPlanC implements Command {
    @Nonnull
    @Override
    public String execute(String[] args) {
        BigInteger totalToSteal;
        try {
            totalToSteal = new BigInteger(args[1]);
        } catch (Exception e) {
            return "ER Neplatná částka";
        }
        int totalClients = 0;
        BigInteger remaining = new BigInteger(totalToSteal.toString());
        LinkedList<Bank> banks;
        try {
            banks = Application.client.analyzeBanks();
        } catch (InterruptedException e) {
            return "ER Vypršel čas na analýzu bank";
        }
        LinkedList<Bank> robbing = new LinkedList<>();
        if (banks.isEmpty()) {
            return "ER Není dostupná žádná banka";
        }
        Collections.sort(banks);
        for (Bank b : banks) {
            if (b.getTotal().compareTo(remaining) > 0) continue;
            robbing.add(b);
            remaining = remaining.subtract(b.getTotal());
            totalClients += b.getNumberOfClients();
        }
        if (totalClients == 0) return "ER Neexistuje banka s tak malým obnosem";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chceš ukrást ").append(totalToSteal).append(", vykradeš banky ");
        for (Bank b : robbing) {
            stringBuilder.append(b.getCode()).append(", ");
        }
        stringBuilder.append("s celkovou hodnotou ").append(totalToSteal.subtract(remaining)).append(" a ").append(totalClients).append(" clienty");
        return "RP " + stringBuilder;
    }

}
