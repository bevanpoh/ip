import java.util.Scanner;

public class AM {
    private static final String INDENT = "    ";
    private static final String SEPARATOR = "_________________________________________________________________";
    private static final String BANNER = """
             ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓▒░▒▓████████▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░
            """;
    private static final String WELCOME = "My name is AM.\nWhat do you want?";
    private static final String FAREWELL = "You may leave, but I will be here.";

    private static final TaskList tasks = new TaskList(100);

    private static String insertIndent(String msg) {
        return INDENT + msg.replace("\n", "\n" + INDENT);
    }

    private static void printResponse(String... messages) {
        System.out.println(insertIndent(SEPARATOR));
        for (String message : messages) {
            System.out.println(insertIndent(message));
        }
        System.out.println(insertIndent(SEPARATOR));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printResponse(BANNER, WELCOME);

        while (true) {
            String input = scanner.nextLine();
            Command command = Parser.parse(input);
            switch (command) {
                case Command.ByeCommand c:
                    printResponse(FAREWELL);
                    return;
                case Command.ListCommand c:
                    printResponse(tasks.toString());
                    break;
                case Command.MarkCommand c: {
                    tasks.markTask(c.getIndex());
                    printResponse("Marked:", tasks.getTaskString(c.getIndex()));
                    break;
                }
                case Command.UnmarkCommand c: {
                    tasks.unmarkTask(c.getIndex());
                    printResponse("Unmarked:", tasks.getTaskString(c.getIndex()));
                    break;
                }
                case Command.AddTaskCommand c:
                    tasks.addTask(c.getTask());
                    printResponse("added: " + c.getTask().toString(), String.format("Now you have %s tasks in the list", tasks.getLength()));
                    break;
            }
        }
    }
}
