import Exceptions.CorruptedDataException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeadlineTask extends Task {
    private final LocalDate by;

    public DeadlineTask(String name, LocalDate by) {
        super(name);
        this.by = by;
    }

    public DeadlineTask(String name, String by) {
        this(name, LocalDate.parse(by));
    }

    private DeadlineTask(String name, LocalDate by, boolean done) {
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

        return new DeadlineTask(parts[2], LocalDate.parse(parts[3]), done);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }
}
