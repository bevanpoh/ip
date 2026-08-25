public class Parser {
    public static Command parse(String input) {
        String[] parts = input.split("\\s", 2);
        String commandType = parts[0];
        switch (commandType) {
            case "bye":
                return new Command.ByeCommand();
            case "list":
                return new Command.ListCommand();
            case "mark": {
                int taskIndex = Integer.parseInt(parts[1]) - 1;
                return new Command.MarkCommand(taskIndex);
            }
            case "unmark": {
                int taskIndex = Integer.parseInt(parts[1]) - 1;
                return new Command.UnmarkCommand(taskIndex);
            }
            default:
                return new Command.AddTaskCommand(new Task(input));
        }
    }
}
