package am.command;

import java.time.format.DateTimeParseException;

import am.task.DeadlineTask;
import am.task.EventTask;
import am.task.TodoTask;

/**
 * Converts textual user commands into typed command objects.
 */
public class CommandParser {
    /**
     * Creates a command parser.
     */
    public CommandParser() {
    }

    /**
     * Parses a complete user command.
     *
     * @param input command text entered by the user
     * @return the corresponding command object
     * @throws InvalidCommandException if the command arguments are malformed
     * @throws UnknownCommandException if the command name is not supported
     */
    public static Command parse(String input) {
        String[] parts = input.split("\\s+", 2);
        String commandType = parts[0].trim();
        String argument = parts.length > 1 ? parts[1] : "";
        switch (commandType) {
            case "bye": {
                if (!argument.trim().isEmpty()) {
                    throw new InvalidCommandException("You messed up the command.");
                }
                return new Command.ByeCommand();
            }
            case "list": {
                if (!argument.trim().isEmpty()) {
                    throw new InvalidCommandException("You messed up the command.");
                }
                return new Command.ListCommand();
            }
            case "past": {
                if (!argument.trim().isEmpty()) {
                    throw new InvalidCommandException("You messed up the command.");
                }
                return new Command.PastCommand();
            }
            case "mark":
                return new Command.MarkCommand(parseTaskIndex(argument));
            case "unmark":
                return new Command.UnmarkCommand(parseTaskIndex(argument));
            case "todo": {
                String name = argument.trim();
                if (name.isEmpty()) {
                    throw new InvalidCommandException("You messed up the command.");
                }
                return new Command.AddTaskCommand(new TodoTask(name));
            }
            case "deadline": {
                String[] args = argument.trim().split("\\s+(?=/)", 2);
                if (args.length < 2) {
                    throw new InvalidCommandException("You messed up the command.");
                }

                String name = args[0].trim();
                String by = parseParameter(args[1], "/by");
                try {
                    return new Command.AddTaskCommand(new DeadlineTask(name, by));
                } catch (DateTimeParseException exception) {
                    throw new InvalidCommandException("When is that?");
                }
            }
            case "event": {
                String[] args = argument.trim().split("\\s+(?=/)", 2);
                if (args.length < 2) {
                    throw new InvalidCommandException("You messed up the command.");
                }

                String name = args[0].trim();
                String from = parseParameter(args[1], "/from");
                String to = parseParameter(args[1], "/to");
                try {
                    return new Command.AddTaskCommand(new EventTask(name, from, to));
                } catch (DateTimeParseException exception) {
                    throw new InvalidCommandException("When is that?");
                }
            }
            case "delete":
                return new Command.DeleteTaskCommand(parseTaskIndex(argument));
            default:
                throw new UnknownCommandException(String.format(
                        "AAAAHHHHHHHHHHHHHHH\nYou can't tell me to '%s'", input));
        }
    }

    /** Converts a one-based user task number into a zero-based list index. */
    private static int parseTaskIndex(String argument) {
        argument = argument.trim();
        if (argument.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }

        int userIndex;
        try {
            userIndex = Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return userIndex - 1;
    }

    /** Extracts the value associated with a command marker. */
    private static String parseParameter(String argument, String marker) {
        String[] tokens = argument.trim().split("\\s+");
        StringBuilder value = new StringBuilder();
        boolean found = false;

        for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
            if (!tokens[tokenIndex].equals(marker)) {
                continue;
            }

            if (found) {
                throw new InvalidCommandException("You messed up the command.");
            }
            found = true;

            int valueIndex = tokenIndex + 1;
            for (; valueIndex < tokens.length && !tokens[valueIndex].startsWith("/"); valueIndex++) {
                if (!value.isEmpty()) {
                    value.append(" ");
                }
                value.append(tokens[valueIndex]);
            }
            tokenIndex = valueIndex - 1;
        }

        if (!found || value.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }

        return value.toString();
    }

}
