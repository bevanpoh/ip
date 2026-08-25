public class TodoTask extends Task {
    public TodoTask(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
