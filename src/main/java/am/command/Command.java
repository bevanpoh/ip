package am.command;

import am.task.Task;

/**
 * Represents a command parsed from user input.
 */
public abstract sealed class Command {
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
