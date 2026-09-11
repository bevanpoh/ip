package am.command;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.task.DeadlineTask;
import am.task.EventTask;
import am.task.TaskEdit;
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
        return switch (commandType) {
            case "bye" -> requireNoArgument(argument, new Command.ByeCommand());
            case "list" -> requireNoArgument(argument, new Command.ListCommand());
            case "past" -> requireNoArgument(argument, new Command.PastCommand());
            case "find" -> parseFindCommand(argument);
            case "edit" -> parseEditCommand(argument);
            case "mark" -> new Command.MarkCommand(parseTaskIndex(argument));
            case "unmark" -> new Command.UnmarkCommand(parseTaskIndex(argument));
            case "todo" -> parseTodoCommand(argument);
            case "deadline" -> parseDeadlineCommand(argument);
            case "event" -> parseEventCommand(argument);
            case "delete" -> new Command.DeleteTaskCommand(parseTaskIndex(argument));
            default -> throw new UnknownCommandException(String.format(
                    "AAAAHHHHHHHHHHHHHHH\nYou can't tell me to '%s'", input));
        };
    }

    /** Parses each edit marker once, preserving whitespace inside descriptions. */
    private static Command parseEditCommand(String argument) {
        String[] parts = argument.trim().split("\\s+", 2);
        if (parts.length != 2 || argument.contains("\n") || argument.contains("\r")) {
            throw new InvalidCommandException("You messed up the command.");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[0]);
        } catch (NumberFormatException exception) {
            throw new InvalidCommandException("You messed up the command.");
        }
        String parameters = parts[1];
        Matcher markers = Pattern.compile("(?<!\\S)/\\S*").matcher(parameters);
        Map<String, String> fields = new HashMap<>();
        String previous = null;
        int valueStart = 0;
        while (markers.find()) {
            if (previous == null && !parameters.substring(0, markers.start()).isBlank()) {
                throw new InvalidCommandException("You messed up the command.");
            }
            if (previous != null) {
                fields.put(previous, parameters.substring(valueStart, markers.start()).trim());
            }
            String marker = markers.group();
            if (!Set.of("/name", "/by", "/from", "/to").contains(marker) || fields.containsKey(marker)) {
                throw new InvalidCommandException("You messed up the command.");
            }
            previous = marker;
            valueStart = markers.end();
        }
        if (previous == null) {
            throw new InvalidCommandException("You messed up the command.");
        }
        fields.put(previous, parameters.substring(valueStart).trim());
        if (fields.values().stream().anyMatch(String::isBlank)) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return new Command.EditCommand(taskNumber, new TaskEdit(fields));
    }

    /** Returns a command after confirming that it has no arguments. */
    private static Command requireNoArgument(String argument, Command command) {
        if (!argument.trim().isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return command;
    }

    /** Parses a find command and validates its keyword. */
    private static Command parseFindCommand(String argument) {
        String keyword = argument.trim();
        if (keyword.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return new Command.FindCommand(keyword);
    }

    /** Parses a todo command and validates its task name. */
    private static Command parseTodoCommand(String argument) {
        String name = argument.trim();
        if (name.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return new Command.AddTaskCommand(new TodoTask(name));
    }

    /** Parses a deadline command and converts its date into a task. */
    private static Command parseDeadlineCommand(String argument) {
        String[] taskParts = splitTaskArguments(argument);
        String name = taskParts[0].trim();
        String by = parseParameter(taskParts[1], "/by");
        try {
            return new Command.AddTaskCommand(new DeadlineTask(name, by));
        } catch (DateTimeParseException exception) {
            throw new InvalidCommandException("When is that?");
        }
    }

    /** Parses an event command and converts its dates into a task. */
    private static Command parseEventCommand(String argument) {
        String[] taskParts = splitTaskArguments(argument);
        String name = taskParts[0].trim();
        String from = parseParameter(taskParts[1], "/from");
        String to = parseParameter(taskParts[1], "/to");
        try {
            return new Command.AddTaskCommand(new EventTask(name, from, to));
        } catch (DateTimeParseException exception) {
            throw new InvalidCommandException("When is that?");
        }
    }

    /** Splits a task name from the parameter section of a command. */
    private static String[] splitTaskArguments(String argument) {
        String[] taskParts = argument.trim().split("\\s+(?=/)", 2);
        if (taskParts.length < 2) {
            throw new InvalidCommandException("You messed up the command.");
        }
        return taskParts;
    }

    /** Converts a one-based user task number into a zero-based list index. */
    private static int parseTaskIndex(String argument) {
        String trimmedArgument = argument.trim();
        if (trimmedArgument.isEmpty()) {
            throw new InvalidCommandException("You messed up the command.");
        }

        int userIndex;
        try {
            userIndex = Integer.parseInt(trimmedArgument);
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
