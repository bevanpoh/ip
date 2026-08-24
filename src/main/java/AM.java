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
            String[] command = input.split("\\s+");
            switch (command[0]) {
                case "bye":
                    printResponse(FAREWELL);
                    return;
                case "list":
                    printResponse(tasks.toString());
                    break;
                case "mark": {
                    int taskIndex = Integer.parseInt(command[1]) - 1;
                    tasks.markTask(taskIndex);
                    printResponse("Marked:", tasks.getTaskString(taskIndex));
                    break;
                }
                case "unmark": {
                    int taskIndex = Integer.parseInt(command[1]) - 1;
                    tasks.unmarkTask(taskIndex);
                    printResponse("Unmarked:", tasks.getTaskString(taskIndex));
                    break;
                }
                default:
                    tasks.addTask(new Task(input));
                    printResponse("added: " + input);
                    break;
            }
        }
    }
}
