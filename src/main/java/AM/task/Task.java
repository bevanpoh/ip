package AM.task;

import AM.storage.CorruptedDataException;

import java.time.LocalDateTime;

/**
 * Stores the completion state and display icon for a task.
 */
enum TaskStatus {
    DONE("[X]"),
    NOT_DONE("[ ]");

    private final String icon;

    /**
     * Creates a status with its display icon.
     *
     * @param icon icon shown beside tasks with this status
     */
    TaskStatus(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the icon associated with this status.
     *
     * @return status icon
     */
    public String getIcon() {
        return icon;
    }
}

/**
 * Base type for tasks that can be completed and persisted.
 */
public abstract class Task {
    private final String name;
    private TaskStatus status;

    /**
     * Creates an incomplete task with the supplied name.
     *
     * @param name task description
     */
    public Task(String name) {
        this(name, false);
    }

    /**
     * Creates a task with an explicitly supplied completion state.
     *
     * @param name task description
     * @param done whether the task is complete
     */
    protected Task(String name, boolean done) {
        this.name = name;
        status = done ? TaskStatus.DONE : TaskStatus.NOT_DONE;
    }

    /**
     * Marks this task as complete.
     */
    public void mark() {
        status = TaskStatus.DONE;
    }

    /**
     * Marks this task as incomplete.
     */
    public void unmark() {
        status = TaskStatus.NOT_DONE;
    }

    /**
     * Converts this task to the line format used by storage.
     *
     * @return serialised task data
     */
    public abstract String toSerialised();

    /**
     * Checks whether this task ended before a given time.
     *
     * <p>Tasks without a time constraint are never considered past.</p>
     *
     * @param datetime time against which the task is checked
     * @return {@code true} if the task is past, otherwise {@code false}
     */
    public boolean isPast(LocalDateTime datetime) {
        return false;
    }

    /**
     * Delegates deserialisation to the factory belonging to the matching subtype.
     *
     * @param line serialised task data
     * @return the reconstructed task
     * @throws CorruptedDataException if the line is empty or has an unknown type
     */
    public static Task fromSerialised(String line) throws CorruptedDataException {
        if (line == null || line.isBlank()) {
            throw new CorruptedDataException("Task data is empty");
        }

        String taskType = line.trim().split("\\s*\\|\\s*", 2)[0];
        return switch (taskType) {
            case "T" -> TodoTask.fromSerialised(line);
            case "D" -> DeadlineTask.fromSerialised(line);
            case "E" -> EventTask.fromSerialised(line);
            default -> throw new CorruptedDataException("Unknown task type: " + taskType);
        };
    }

    /**
     * Returns the compact completion status used in persisted data.
     *
     * @return {@code "1"} for complete or {@code "0"} for incomplete
     */
    protected String getSerialisedStatus() {
        return status == TaskStatus.DONE ? "1" : "0";
    }

    /**
     * Returns the task description for subclasses.
     *
     * @return task description
     */
    protected String getName() {
        return name;
    }

    /**
     * Formats the task for display to the user.
     *
     * @return display representation of the task
     */
    @Override
    public String toString() {
        return String.format("%s %s", status.getIcon(), name);
    }
}
