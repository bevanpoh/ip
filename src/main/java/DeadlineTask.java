import Exceptions.CorruptedDataException;

public class DeadlineTask extends Task {
    private final String by;

    public DeadlineTask(String name, String by) {
        super(name);
        this.by = by;
    }

    private DeadlineTask(String name, String by, boolean done) {
        super(name, done);
        this.by = by;
    }

    @Override
    public String toSerialised() {
        return String.format("D | %s | %s | %s", getSerialisedStatus(), getName(), by);
    }

    public static DeadlineTask fromSerialised(String line) throws CorruptedDataException {
        if (line == null || line.isBlank()) {
            throw new CorruptedDataException("Deadline task data is empty");
        }

        String[] parts = line.trim().split("\\s*\\|\\s*", -1);
        if (parts.length != 4 || !parts[0].equals("D")) {
            throw new CorruptedDataException("Malformed deadline task data: " + line);
        }

        boolean done;
        switch (parts[1]) {
            case "0":
                done = false;
                break;
            case "1":
                done = true;
                break;
            default:
                throw new CorruptedDataException("Unknown deadline task status: " + parts[1]);
        }

        return new DeadlineTask(parts[2], parts[3], done);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), by);
    }
}
