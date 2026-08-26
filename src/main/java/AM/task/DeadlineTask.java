package AM.task;

import AM.storage.CorruptedDataException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DeadlineTask extends Task {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private final LocalDateTime by;

    public DeadlineTask(String name, LocalDateTime by) {
        super(name);
        this.by = by;
    }

    public DeadlineTask(String name, String by) {
        this(name, parseDateTime(by));
    }

    private DeadlineTask(String name, LocalDateTime by, boolean done) {
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

        return new DeadlineTask(parts[2], LocalDateTime.parse(parts[3]), done);
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                by.format(DISPLAY_FORMAT));
    }

    @Override
    public boolean isPast(LocalDateTime datetime) {
        return by.isBefore(datetime);
    }

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, INPUT_FORMAT);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(value, DATE_FORMAT).atTime(LocalTime.of(23, 59));
        }
    }
}
