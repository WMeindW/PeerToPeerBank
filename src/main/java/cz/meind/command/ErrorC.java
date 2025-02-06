package cz.meind.command;

import javax.annotation.Nonnull;

public class ErrorC implements Command {
    @Nonnull
    @Override
    public String execute(String[] args) {
        return "ER Neznámý příkaz: " + args[0];
    }
}
