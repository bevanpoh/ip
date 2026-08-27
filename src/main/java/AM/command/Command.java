package AM.command;

import AM.task.Task;

// TODO: This design aims to reduce file bloat for the different command types
// The commands are primarily meant to be data carriers and don't contain
// any execution logic, that should be handled by the chatbot I feel.

// An alternative design is to use a sealed interface and inner record classes
// instead of a sealed class with static inner classes.
// The alternative will result in more concise code,
// but using interface without defining a shared method contract feels weird to me
// so this will have to do for now
/**
 * Represents a command parsed from user input.
 */
public abstract sealed class Command {
    // Private constructor locks inheritance to this file
    private Command() {
    }

    /**
     * Requests that the application terminate.
     */
    public static final class ByeCommand extends Command {
        /**
         * Creates a command that terminates the application.
         */
        public ByeCommand() {
        }
    }

    /**
     * Requests that all tasks be displayed.
     */
    public static final class ListCommand extends Command {
        /**
         * Creates a command that displays all tasks.
         */
        public ListCommand() {
        }
    }

    /**
     * Requests that past tasks be displayed.
     */
    public static final class PastCommand extends Command {
        /**
         * Creates a command that displays past tasks.
         */
        public PastCommand() {
        }
    }

    /**
     * Marks a task as complete.
     */
    public static final class MarkCommand extends Command {
        private final int index;

        /**
         * Creates a command targeting a task index.
         *
         * @param index zero-based task index
         */
        public MarkCommand(int index) {
            this.index = index;
        }

        /**
         * Returns the zero-based index of the task to mark.
         *
         * @return task index
         */
        public int getIndex() {
            return index;
        }
    }

    /**
     * Marks a task as incomplete.
     */
    public static final class UnmarkCommand extends Command {
        private final int index;

        /**
         * Creates a command targeting a task index.
         *
         * @param index zero-based task index
         */
        public UnmarkCommand(int index) {
            this.index = index;
        }

        /**
         * Returns the zero-based index of the task to unmark.
         *
         * @return task index
         */
        public int getIndex() {
            return index;
        }
    }

    /**
     * Adds a task to the task list.
     */
    public static final class AddTaskCommand extends Command {
        private final Task task;

        /**
         * Creates a command carrying the task to add.
         *
         * @param task task to add
         */
        public AddTaskCommand(Task task) {
            this.task = task;
        }

        /**
         * Returns the task carried by this command.
         *
         * @return task to add
         */
        public Task getTask() {
            return task;
        }
    }

    /**
     * Removes a task from the task list.
     */
    public static final class DeleteTaskCommand extends Command {
        private final int index;

        /**
         * Creates a command targeting a task index.
         *
         * @param index zero-based task index
         */
        public DeleteTaskCommand(int index) {
            this.index = index;
        }

        /**
         * Returns the zero-based index of the task to delete.
         *
         * @return task index
         */
        public int getIndex() {
            return index;
        }
    }
}
