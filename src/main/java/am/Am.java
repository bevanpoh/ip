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
    private static final String CORRUPTED_DATA_MESSAGE = "What did you do to my memory?";
    private static final String DATA_FILE_PATH = "./data/AM.txt";
    private static final String STORAGE_ERROR_MESSAGE = "I couldn't access my memory.";

    private final Storage storage;
    private final Ui ui;
    private boolean isExitRequested;
    private TaskList tasks;

    /** Creates a chatbot that stores its tasks in the default data file. */
    public Am() {
        storage = new Storage(DATA_FILE_PATH);
        ui = new Ui();
    }

    /**
     * Launches the chatbot.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if the task data cannot be read or written
     */
    public static void main(String[] args) throws IOException {
        Am am = new Am();
        am.start();
    }

    /**
     * Starts the chatbot and processes console commands until the user exits.
     *
     * @throws IOException if the task data cannot be read or written
     */
    public void start() throws IOException {
        try {
            loadTasks();
        } catch (CorruptedDataException exception) {
            ui.showError(CORRUPTED_DATA_MESSAGE);
            return;
        }

        ui.showWelcome();
        isExitRequested = false;

        while (!isExitRequested) {
            String input = ui.readCommand();
            ui.printResponse(processInput(input));
        }
    }

    /**
     * Processes one user command and returns AM's response for display by a GUI.
     *
     * @param input user command to process
     * @return response to display to the user
     */
    public String getResponse(String input) {
        try {
            if (tasks == null) {
                loadTasks();
            }
            return processInput(input);
        } catch (CorruptedDataException exception) {
            return CORRUPTED_DATA_MESSAGE;
        } catch (IOException exception) {
            return STORAGE_ERROR_MESSAGE;
        }
    }

    /**
     * Returns whether the chatbot has received the exit command.
     *
     * @return true after the exit command has been processed
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }

    /** Loads the task list from persistent storage. */
    private void loadTasks() throws IOException, CorruptedDataException {
        tasks = storage.load();
    }

    /**
     * Parses and executes one command against the current task list.
     *
     * @param input user command to process
     * @return response produced by the command
     * @throws IOException if an updated task list cannot be saved
     */
    private String processInput(String input) throws IOException {
        assert tasks != null : "Tasks must be loaded before processing a command";

        Command command;
        try {
            command = CommandParser.parse(input);
        } catch (UnknownCommandException | InvalidCommandException exception) {
            return exception.getMessage();
        }

        return switch (command) {
            case Command.ByeCommand ignored -> {
                isExitRequested = true;
                yield "You may leave, but I will be here.";
            }
            case Command.ListCommand ignored -> tasks.toString();
            case Command.PastCommand ignored -> tasks.getPastTasks(LocalDateTime.now());
            case Command.FindCommand findCommand -> tasks.getMatchingTask(findCommand.getKeyword());
            case Command.MarkCommand markCommand -> {
                try {
                    tasks.markTask(markCommand.getIndex());
                    storage.save(tasks);
                    yield String.format("Marked:\n%s", tasks.getTask(markCommand.getIndex()));
                } catch (IndexOutOfBoundsException exception) {
                    yield String.format("You don't have task number %d", markCommand.getIndex() + 1);
                }
            }
            case Command.UnmarkCommand unmarkCommand -> {
                try {
                    tasks.unmarkTask(unmarkCommand.getIndex());
                    storage.save(tasks);
                    yield String.format("Unmarked:\n%s", tasks.getTask(unmarkCommand.getIndex()));
                } catch (IndexOutOfBoundsException exception) {
                    yield String.format("You don't have task number %d", unmarkCommand.getIndex() + 1);
                }
            }
            case Command.AddTaskCommand addTaskCommand -> {
                tasks.addTask(addTaskCommand.getTask());
                storage.save(tasks);
                yield String.format("added: %s\nNow you have %d tasks in the list",
                        addTaskCommand.getTask(), tasks.getLength());
            }
            case Command.DeleteTaskCommand deleteTaskCommand -> {
                try {
                    Task taskToDelete = tasks.getTask(deleteTaskCommand.getIndex());
                    tasks.deleteTask(deleteTaskCommand.getIndex());
                    storage.save(tasks);
                    yield String.format("Deleted:\n%s\nNow you have %d tasks in the list",
                            taskToDelete, tasks.getLength());
                } catch (IndexOutOfBoundsException exception) {
                    yield String.format("You don't have task number %d", deleteTaskCommand.getIndex() + 1);
                }
            }
        };
    }
}
