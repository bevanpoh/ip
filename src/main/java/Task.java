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

public class Task {
    private final String name;
    private TaskStatus status;

    public Task(String name) {
        this.name = name;
        status = TaskStatus.NOT_DONE;
    }

    public void mark() {
        status = TaskStatus.DONE;
    }

    public void unmark() {
        status = TaskStatus.NOT_DONE;
    }

    @Override
    public String toString() {
        return String.format("%s %s", status.getIcon(), name);
    }
}