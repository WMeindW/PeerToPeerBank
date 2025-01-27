package cz.meind.command;

import cz.meind.client.Client;

public class RobberyPlanC implements Command {
    @Override
    public String execute(String[] args) {
        return Client.scanNetwork().toString();
    }
}
