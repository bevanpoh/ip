package am;

import java.io.IOException;
import java.time.LocalDateTime;

import am.command.Command;
import am.command.CommandParser;
import am.command.InvalidCommandException;
import am.command.UnknownCommandException;
import am.storage.CorruptedDataException;
import am.storage.Storage;
import am.task.Task;
import am.task.TaskList;
import am.ui.Ui;

/** Runs the AM command-line task manager. */
public class Am {
    private static final String DATA_FILE_PATH = "./data/AM.txt";

    /** Starts the task manager and processes commands until the user exits. */
    public static void main(String[] args) throws IOException {
        Storage storage = new Storage(DATA_FILE_PATH);
        Ui ui = new Ui();
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (CorruptedDataException exception) {
            ui.showError("What did you do to my memory?");
            return;
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            Command command;
            try {
                command = CommandParser.parse(input);
            } catch (UnknownCommandException | InvalidCommandException exception) {
                ui.showError(exception.getMessage());
                continue;
            }
            switch (command) {
                case Command.ByeCommand ignored:
                    ui.printResponse("You may leave, but I will be here.");
                    return;
                case Command.ListCommand ignored:
                    ui.printResponse(tasks.toString());
                    break;
                case Command.PastCommand ignored:
                    ui.printResponse(tasks.getPastTasks(LocalDateTime.now()));
                    break;
                case Command.FindCommand findCommand:
                    ui.printResponse(tasks.getMatchingTask(findCommand.getKeyword()));
                    break;
                case Command.MarkCommand markCommand: {
                    try {
                        tasks.markTask(markCommand.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Marked:\n%s",
                                tasks.getTask(markCommand.getIndex())));
                    } catch (IndexOutOfBoundsException exception) {
                        ui.showError(String.format("You don't have task number %d",
                                markCommand.getIndex() + 1));
                    }
                    break;
                }
                case Command.UnmarkCommand unmarkCommand: {
                    try {
                        tasks.unmarkTask(unmarkCommand.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Unmarked:\n%s",
                                tasks.getTask(unmarkCommand.getIndex())));
                    } catch (IndexOutOfBoundsException exception) {
                        ui.showError(String.format("You don't have task number %d",
                                unmarkCommand.getIndex() + 1));
                    }
                    break;
                }
                case Command.AddTaskCommand addTaskCommand:
                    tasks.addTask(addTaskCommand.getTask());
                    storage.save(tasks);
                    ui.printResponse(String.format("added: %s\nNow you have %d tasks in the list",
                            addTaskCommand.getTask(),
                            tasks.getLength()));
                    break;
                case Command.DeleteTaskCommand deleteTaskCommand: {
                    try {
                        Task taskToDelete = tasks.getTask(deleteTaskCommand.getIndex());
                        tasks.deleteTask(deleteTaskCommand.getIndex());
                        storage.save(tasks);
                        ui.printResponse(String.format("Deleted:\n%s\nNow you have %d tasks in the list",
                                taskToDelete,
                                tasks.getLength()));
                    } catch (IndexOutOfBoundsException exception) {
                        ui.showError(String.format("You don't have task number %d",
                                deleteTaskCommand.getIndex() + 1));
                    }
                    break;
                }
            }
        }
    }
}
