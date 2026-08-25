public class Parser {
    public static Command parse(String input) {
        String[] parts = input.split("\\s+", 2);
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
            case "todo":
                return new Command.AddTaskCommand(new TodoTask(parts[1]));
            case "deadline": {
                String[] args = parts[1].split(" /by ", 2);
                String name = args[0].trim();
                String by = args[1].trim();
                return new Command.AddTaskCommand(new DeadlineTask(name, by));
            }
            case "event": {
                String[] args = parts[1].split(" /from | /to ");
                String name = args[0].trim();
                String from = args[1].trim();
                String to = args[2].trim();
                return new Command.AddTaskCommand(new EventTask(name, from, to));
            }
            default:
                return new Command.AddTaskCommand(new Task(input));
        }
    }
}
