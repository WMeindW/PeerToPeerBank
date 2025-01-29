package cz.meind.command;

import cz.meind.client.Bank;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;

public class RobberyPlanC implements Command {
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
        LinkedList<Bank> banks = new LinkedList<>();
        LinkedList<Bank> robbing = new LinkedList<>();
        banks.add(new Bank("1", new BigInteger("500000"), 10));
        banks.add(new Bank("2", new BigInteger("5000000"), 15));
        banks.add(new Bank("3", new BigInteger("835956"), 10));
        banks.add(new Bank("4", new BigInteger("23683333"), 4));
        banks.add(new Bank("5", new BigInteger("34984413275"), 16));
        banks.add(new Bank("6", new BigInteger("278"), 10));
        banks.add(new Bank("7", new BigInteger("500000"), 10));
        if (banks.size() <= 0) {
            return "ER Není dostupná žádná banka";
        }
        Collections.sort(banks);
        for (Bank b : banks) {
            if (b.getTotal().compareTo(remaining) > 0) continue;
            robbing.add(b);
            System.out.println(b.getTotal());
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
