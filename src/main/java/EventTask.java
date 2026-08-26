import Exceptions.CorruptedDataException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventTask extends Task {
    private final LocalDate from;
    private final LocalDate to;

    public EventTask(String name, LocalDate from, LocalDate to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public EventTask(String name, String from, String to) {
        this(name, LocalDate.parse(from), LocalDate.parse(to));
    }

    private EventTask(String name, LocalDate from, LocalDate to, boolean done) {
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

        return new EventTask(parts[2], LocalDate.parse(parts[3]), LocalDate.parse(parts[4]), done);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                from.format(DateTimeFormatter.ofPattern("MMM d yyyy")),
                to.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }
}
