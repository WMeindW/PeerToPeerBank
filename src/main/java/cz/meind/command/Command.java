package cz.meind.command;

import javax.annotation.Nonnull;

public interface Command {

    @Nonnull
    String execute(String[] args);
}
