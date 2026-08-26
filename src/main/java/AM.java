import Exceptions.CorruptedDataException;
import Exceptions.InvalidCommandException;
import Exceptions.UnknownCommandException;

import java.time.LocalDateTime;

public class AM {
    private static final String DATA_FILE_PATH = "./data/AM.txt";

    public static void main(String[] args) throws java.io.IOException {
        Storage storage = new Storage(DATA_FILE_PATH);
        UI ui = new UI();
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (CorruptedDataException err) {
            ui.showError("What did you do to my memory?");
            return;
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            Command command;
            try {
                command = CommandParser.parse(input);
            } catch (UnknownCommandException | InvalidCommandException err) {
                ui.showError(err.getMessage());
                continue;
            }
            switch (command) {
                case Command.ByeCommand c:
                    ui.printResponse("You may leave, but I will be here.");
                    return;
                case Command.ListCommand c:
                    ui.printResponse(tasks.toString());
                    break;
                case Command.PastCommand c:
                    ui.printResponse(tasks.getPastTasks(LocalDateTime.now()));
                    break;
                case Command.MarkCommand c: {
                    try {
                        tasks.markTask(c.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Marked:\n%s", tasks.getTask(c.getIndex())));
                    } catch (IndexOutOfBoundsException err) {
                        ui.showError(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.UnmarkCommand c: {
                    try {
                        tasks.unmarkTask(c.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Unmarked:\n%s", tasks.getTask(c.getIndex())));
                    } catch (IndexOutOfBoundsException err) {
                        ui.showError(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
                case Command.AddTaskCommand c:
                    tasks.addTask(c.getTask());
                    storage.save(tasks);
                    ui.printResponse(String.format("added: %s\nNow you have %d tasks in the list",
                            c.getTask().toString(),
                            tasks.getLength()));
                    break;
                case Command.DeleteTaskCommand c: {
                    try {
                        Task toDelete = tasks.getTask(c.getIndex());
                        tasks.deleteTask(c.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Deleted:\n%s\nNow you have %d tasks in the list",
                                toDelete,
                                tasks.getLength()));
                    } catch (IndexOutOfBoundsException err) {
                        ui.showError(String.format("You don't have task number %d", c.getIndex() + 1));
                    }
                    break;
                }
            }
        }
    }
}
