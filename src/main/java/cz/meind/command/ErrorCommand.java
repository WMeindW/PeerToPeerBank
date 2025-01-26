package cz.meind.command;

import java.util.Arrays;

public class ErrorCommand implements Command {
    @Override
    public String execute(String[] args) {
        return "Error with command: " + Arrays.toString(args);
    }
}
