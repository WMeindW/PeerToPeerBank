package cz.meind.command;

import cz.meind.application.Application;
import cz.meind.client.Client;

public class RobberyPlanC implements Command {
    @Override
    public String execute(String[] args) {
        try {
            return Application.client.analyzeBanks().toString();
        } catch (InterruptedException e) {
            return "ER Nastala chyba při prohledávání síťě";
        }
    }
}
