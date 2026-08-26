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
public abstract sealed class Command {
    // Private constructor locks inheritance to this file
    private Command() {
    }

    public static final class ByeCommand extends Command {
    }

    public static final class ListCommand extends Command {
    }

    public static final class PastCommand extends Command {
    }

    public static final class MarkCommand extends Command {
        private final int index;

        public MarkCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static final class UnmarkCommand extends Command {
        private final int index;

        public UnmarkCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static final class AddTaskCommand extends Command {
        private final Task task;

        public AddTaskCommand(Task task) {
            this.task = task;
        }

        public Task getTask() {
            return task;
        }
    }

    public static final class DeleteTaskCommand extends Command {
        private final int index;

        public DeleteTaskCommand(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
