import Exceptions.MissingArgumentException;
import Exceptions.TaskListLengthExceededException;
import Exceptions.UnknownCommandException;

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
            ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓░""";
    private static final TaskList tasks = new TaskList();

    private static String insertIndent(String msg) {
        return INDENT + msg.replace("\n", "\n" + INDENT);
    }

    private static void printResponse(String message) {
        System.out.println(insertIndent(SEPARATOR));
        System.out.println(insertIndent(message));
        System.out.println(insertIndent(SEPARATOR));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printResponse(String.format("%s\nMy name is AM.\nWhat do you want?", BANNER));

        while (true) {
            String input = scanner.nextLine();
            Command command;
            try {
                command = Parser.parse(input);
            } catch (UnknownCommandException | MissingArgumentException err) {
                printResponse(err.getMessage());
                continue;
            }
            switch (command) {
                case Command.ByeCommand c:
                    printResponse("You may leave, but I will be here.");
                    return;
                case Command.ListCommand c:
                    printResponse(tasks.toString());
                    break;
                case Command.MarkCommand c: {
                    try {
                        tasks.markTask(c.getIndex());
                        printResponse(String.format("Marked:\n%s", tasks.getTaskString(c.getIndex())));
                    } catch (TaskListLengthExceededException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.UnmarkCommand c: {
                    try {
                        tasks.unmarkTask(c.getIndex());
                        printResponse(String.format("Unmarked:\n%s", tasks.getTaskString(c.getIndex())));
                    } catch (TaskListLengthExceededException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.AddTaskCommand c:
                    tasks.addTask(c.getTask());
                    printResponse(String.format("added: %s\nNow you have %d tasks in the list",
                            c.getTask().toString(),
                            tasks.getLength()));
                    break;
                case Command.DeleteTaskCommand c: {
                    try {
                        String toDelete = tasks.getTaskString(c.getIndex());
                        tasks.deleteTask(c.getIndex());
                        printResponse(String.format("Deleted:\n%s\nNow you have %d tasks in the list",
                                toDelete,
                                tasks.getLength()));
                    } catch (TaskListLengthExceededException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
            }
        }
    }
}
