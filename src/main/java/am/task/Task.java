package am.task;

import java.time.LocalDateTime;

import am.storage.CorruptedDataException;

/** Represents the completion state used for task serialization. */
enum TaskStatus {
    DONE("[X]"),
    NOT_DONE("[ ]");

    private final String icon;

    /** Creates a status with its display icon. */
    TaskStatus(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}

/** Provides shared state and behavior for all task types. */
public abstract class Task {
    private final String name;
    private TaskStatus status;

    /** Creates an unfinished task with the given name. */
    public Task(String name) {
        this(name, false);
    }

    protected Task(String name, boolean done) {
        this.name = name;
        status = done ? TaskStatus.DONE : TaskStatus.NOT_DONE;
    }

    /** Marks this task as done. */
    public void mark() {
        status = TaskStatus.DONE;
    }

    /** Marks this task as not done. */
    public void unmark() {
        status = TaskStatus.NOT_DONE;
    }

    /** Serializes this task into one storage record. */
    public abstract String toSerialized();

    /** Returns whether this task's scheduled time has passed. */
    public boolean isPast(LocalDateTime datetime) {
        return false;
    }

    /**
     * Delegates deserialization to the factory belonging to the matching subtype.
     *
     * @param line serialized task data
     * @return the reconstructed task
     * @throws CorruptedDataException if the line is empty or has an unknown type
     */
    public static Task fromSerialized(String line) throws CorruptedDataException {
        if (line == null || line.isBlank()) {
            throw new CorruptedDataException("Task data is empty");
        }

        String taskType = line.trim().split("\\s*\\|\\s*", 2)[0];
        return switch (taskType) {
            case "T" -> TodoTask.fromSerialized(line);
            case "D" -> DeadlineTask.fromSerialized(line);
            case "E" -> EventTask.fromSerialized(line);
            default -> throw new CorruptedDataException("Unknown task type: " + taskType);
        };
    }

    /** Returns the serialized completion status used by storage. */
    protected String getSerializedStatus() {
        return status == TaskStatus.DONE ? "1" : "0";
    }

    protected String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s %s", status.getIcon(), name);
    }
}
