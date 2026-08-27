package am.command;

import am.task.Task;

/** Represents a parsed command and its optional data. */
public abstract sealed class Command {
    private Command() {
    }

    /** Represents a command that exits the application. */
    public static final class ByeCommand extends Command {
    }

    /** Represents a command that lists all tasks. */
    public static final class ListCommand extends Command {
    }

    /** Represents a command that lists tasks whose scheduled time has passed. */
    public static final class PastCommand extends Command {
    }

    /** Represents a command that marks a task as done. */
    public static final class MarkCommand extends Command {
        private final int index;

        /** Creates a mark command for the given zero-based task index. */
        public MarkCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    /** Represents a command that marks a task as not done. */
    public static final class UnmarkCommand extends Command {
        private final int index;

        /** Creates an unmark command for the given zero-based task index. */
        public UnmarkCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    /** Represents a command that adds a task. */
    public static final class AddTaskCommand extends Command {
        private final Task task;

        /** Creates an add command for the given task. */
        public AddTaskCommand(Task task) {
            this.task = task;
        }

        public Task getTask() {
            return task;
        }
    }

    /** Represents a command that deletes a task. */
    public static final class DeleteTaskCommand extends Command {
        private final int index;

        /** Creates a delete command for the given zero-based task index. */
        public DeleteTaskCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
