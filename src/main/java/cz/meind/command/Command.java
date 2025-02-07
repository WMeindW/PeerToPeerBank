package cz.meind.command;

import javax.annotation.Nonnull;

/**
 * Represents a command that can be executed by the application.
 *
 * @author Tabnine Protected
 */
public interface Command {

    /**
     * Executes the command with the given arguments.
     *
     * @param args The command-line arguments provided to the command.
     * @return A string representing the result of the command execution.
     *         The returned string should not be null.
     */
    @Nonnull
    String execute(String[] args);
}
