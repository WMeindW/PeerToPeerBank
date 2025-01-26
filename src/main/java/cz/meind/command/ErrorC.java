package cz.meind.command;

public class ErrorC implements Command {
    @Override
    public String execute(String[] args) {
        return "ER Neznámý příkaz: " + args[0];
    }
}
