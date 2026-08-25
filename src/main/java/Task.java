public class Task {
    private final String name;
    private boolean isDone = false;

    public Task(String name) {
        this.name = name;
    }

    public void mark() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    @Override
    public String toString() {
        String doneStatus;
        if (isDone) {
            doneStatus = "[X]";
        } else {
            doneStatus = "[ ]";
        }

        return String.format("%s %s", doneStatus, name);
    }
}