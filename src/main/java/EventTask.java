import Exceptions.CorruptedDataException;

public class EventTask extends Task {
    private final String from;
    private final String to;

    public EventTask(String name, String from, String to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    private EventTask(String name, String from, String to, boolean done) {
        super(name, done);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toSerialised() {
        return String.format("E | %s | %s | %s | %s", getSerialisedStatus(), getName(), from, to);
    }

    public static EventTask fromSerialised(String line) throws CorruptedDataException {
        if (line == null || line.isBlank()) {
            throw new CorruptedDataException("Event task data is empty");
        }

        String[] parts = line.trim().split("\\s*\\|\\s*", -1);
        if (parts.length != 5 || !parts[0].equals("E")) {
            throw new CorruptedDataException("Malformed event task data: " + line);
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
                throw new CorruptedDataException("Unknown event task status: " + parts[1]);
        }

        return new EventTask(parts[2], parts[3], parts[4], done);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), from, to);
    }
}
