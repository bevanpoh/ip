import Exceptions.CorruptedDataException;

import java.time.LocalDateTime;

enum TaskStatus {
    DONE("[X]"),
    NOT_DONE("[ ]");

    private final String icon;

    TaskStatus(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}

public abstract class Task {
    private final String name;
    private TaskStatus status;

    public Task(String name) {
        this(name, false);
    }

    protected Task(String name, boolean done) {
        this.name = name;
        status = done ? TaskStatus.DONE : TaskStatus.NOT_DONE;
    }

    public void mark() {
        status = TaskStatus.DONE;
    }

    public void unmark() {
        status = TaskStatus.NOT_DONE;
    }

    public abstract String toSerialised();

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

    protected String getSerialisedStatus() {
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
