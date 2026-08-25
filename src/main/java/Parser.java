import Exceptions.MissingArgumentException;
import Exceptions.UnknownCommandException;

public class Parser {
    public static Command parse(String input) throws UnknownCommandException, MissingArgumentException {
        String[] parts = input.split("\\s", 2);
        String commandType = parts[0].trim();
        switch (commandType) {
            case "bye":
                return new Command.ByeCommand();
            case "list":
                return new Command.ListCommand();
            case "mark": {
                int taskIndex;
                try {
                    taskIndex = Integer.parseInt(parts[1].trim()) - 1;
                } catch (ArrayIndexOutOfBoundsException err) {
                    throw new MissingArgumentException("You messed up the command");
                }
                return new Command.MarkCommand(taskIndex);
            }
            case "unmark": {
                int taskIndex;
                try {
                    taskIndex = Integer.parseInt(parts[1].trim()) - 1;
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException err) {
                    throw new MissingArgumentException("You messed up the command");
                }
                return new Command.UnmarkCommand(taskIndex);
            }
            case "todo": {
                String name;
                try {
                    name = parts[1].trim();
                } catch (ArrayIndexOutOfBoundsException err) {
                    throw new MissingArgumentException("You messed up the command");
                }
                return new Command.AddTaskCommand(new TodoTask(name));
            }
            case "deadline": {
                String[] args;
                String name, by;
                try {
                    args = parts[1].split(" /by ", 2);
                    name = args[0].trim();
                    by = args[1].trim();
                } catch (ArrayIndexOutOfBoundsException err) {
                    throw new MissingArgumentException("You messed up the command");
                }
                return new Command.AddTaskCommand(new DeadlineTask(name, by));
            }
            case "event": {
                String[] args;
                String name, from, to;
                try {
                    args = parts[1].split(" /from | /to ");
                    name = args[0].trim();
                    from = args[1].trim();
                    to = args[2].trim();
                } catch (ArrayIndexOutOfBoundsException err) {
                    throw new MissingArgumentException("You messed up the command");
                }
                return new Command.AddTaskCommand(new EventTask(name, from, to));
            }
            default:
                throw new UnknownCommandException(String.format("AAAAHHHHHHHHHHHHHHH\nYou can't tell me to '%s'", input));
        }
    }
}
