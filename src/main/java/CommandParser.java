import Exceptions.InvalidCommandException;
import Exceptions.UnknownCommandException;

public class CommandParser {
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
                return new Command.AddTaskCommand(new DeadlineTask(name, by));
            }
            case "event": {
                String[] args = argument.trim().split("\\s+(?=/)", 2);
                if (args.length < 2) {
                    throw new InvalidCommandException("You messed up the command.");
                }

                String name = args[0].trim();
                String from = parseParameter(args[1], "/from");
                String to = parseParameter(args[1], "/to");
                return new Command.AddTaskCommand(new EventTask(name, from, to));
            }
            case "delete":
                return new Command.DeleteTaskCommand(parseTaskIndex(argument));
            default:
                throw new UnknownCommandException(String.format("AAAAHHHHHHHHHHHHHHH\nYou can't tell me to '%s'", input));
        }
    }

    private static int parseTaskIndex(String argument) {
        argument = argument.trim();
        if (argument.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }

        int userIndex;
        try {
            userIndex = Integer.parseInt(argument);
        } catch (NumberFormatException error) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return userIndex - 1;
    }

    private static String parseParameter(String argument, String marker) {
        String[] tokens = argument.trim().split("\\s+");
        StringBuilder value = new StringBuilder();
        boolean found = false;

        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].equals(marker)) {
                continue;
            }

            if (found) {
                throw new InvalidCommandException("You messed up the command.");
            }
            found = true;

            for (i++; i < tokens.length && !tokens[i].startsWith("/"); i++) {
                if (!value.isEmpty()) {
                    value.append(" ");
                }
                value.append(tokens[i]);
            }
        }

        if (!found || value.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }

        return value.toString();
    }

}
