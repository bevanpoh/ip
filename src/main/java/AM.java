import Exceptions.CorruptedDataException;
import Exceptions.InvalidCommandException;
import Exceptions.UnknownCommandException;

import java.time.LocalDateTime;
import java.util.Scanner;

public class AM {
    private static final String DATA_FILE_PATH = "./data/AM.txt";
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
    private static String insertIndent(String msg) {
        return INDENT + msg.replace("\n", "\n" + INDENT);
    }

    private static void printResponse(String message) {
        System.out.println(insertIndent(SEPARATOR));
        System.out.println(insertIndent(message));
        System.out.println(insertIndent(SEPARATOR));
    }

    public static void main(String[] args) throws java.io.IOException {
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (CorruptedDataException error) {
            printResponse("What did you do to my memory?");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        printResponse(String.format("%s\nMy name is AM.\nWhat do you want?", BANNER));

        while (true) {
            String input = scanner.nextLine();
            Command command;
            try {
                command = CommandParser.parse(input);
            } catch (UnknownCommandException | InvalidCommandException err) {
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
                case Command.PastCommand c:
                    printResponse(tasks.getPastTasks(LocalDateTime.now()));
                    break;
                case Command.MarkCommand c: {
                    try {
                        tasks.markTask(c.getIndex());
                        storage.save(tasks);
                        printResponse(String.format("Marked:\n%s", tasks.getTask(c.getIndex())));
                    } catch (IndexOutOfBoundsException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.UnmarkCommand c: {
                    try {
                        tasks.unmarkTask(c.getIndex());
                        storage.save(tasks);
                        printResponse(String.format("Unmarked:\n%s", tasks.getTask(c.getIndex())));
                    } catch (IndexOutOfBoundsException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.AddTaskCommand c:
                    tasks.addTask(c.getTask());
                    storage.save(tasks);
                    printResponse(String.format("added: %s\nNow you have %d tasks in the list",
                            c.getTask().toString(),
                            tasks.getLength()));
                    break;
                case Command.DeleteTaskCommand c: {
                    try {
                        Task toDelete = tasks.getTask(c.getIndex());
                        tasks.deleteTask(c.getIndex());
                        storage.save(tasks);
                        printResponse(String.format("Deleted:\n%s\nNow you have %d tasks in the list",
                                toDelete,
                                tasks.getLength()));
                    } catch (IndexOutOfBoundsException err) {
                        printResponse(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
            }
        }
    }
}
