package cz.meind.command;

public class ErrorC implements Command {
    @Override
    public String execute(String[] args) {
        return "Invalid command: " + args[0];
    }
}
