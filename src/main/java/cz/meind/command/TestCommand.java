package cz.meind.command;

import java.util.Arrays;

public class TestCommand implements Command {
    @Override
    public String execute(String[] args) {
        return Arrays.toString(Arrays.copyOfRange(args, 1, args.length)) + " test";
    }
}
