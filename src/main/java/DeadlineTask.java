public class DeadlineTask extends Task {
    private final String by;

    public DeadlineTask(String name, String by) {
        super(name);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), by);
    }
}
