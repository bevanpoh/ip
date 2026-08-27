package am.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import am.storage.CorruptedDataException;

/** Represents a task that occurs between a start and end date and time. */
public class EventTask extends Task {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /** Creates an event task with already parsed start and end values. */
    public EventTask(String name, LocalDateTime from, LocalDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    /** Creates an event task by parsing date or date-time strings. */
    public EventTask(String name, String from, String to) {
        this(name, parseDateTime(from, LocalTime.MIDNIGHT),
                parseDateTime(to, LocalTime.of(23, 59)));
    }

    private EventTask(String name, LocalDateTime from, LocalDateTime to, boolean done) {
        super(name, done);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toSerialized() {
        return String.format("E | %s | %s | %s | %s", getSerializedStatus(), getName(), from, to);
    }

    /** Reconstructs an event task from one serialized storage record. */
    public static EventTask fromSerialized(String line) throws CorruptedDataException {
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

        return new EventTask(parts[2], LocalDateTime.parse(parts[3]),
                LocalDateTime.parse(parts[4]), done);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                from.format(DISPLAY_FORMAT), to.format(DISPLAY_FORMAT));
    }

    @Override
    public boolean isPast(LocalDateTime datetime) {
        return to.isBefore(datetime);
    }

    /** Parses a supported date or date-time string with a suitable fallback time. */
    private static LocalDateTime parseDateTime(String value, LocalTime fallbackTime) {
        try {
            return LocalDateTime.parse(value, INPUT_FORMAT);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(value, DATE_FORMAT).atTime(fallbackTime);
        }
    }
}
