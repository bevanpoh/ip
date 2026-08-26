package AM.task;

import AM.storage.CorruptedDataException;

public class TodoTask extends Task {
    public TodoTask(String name) {
        super(name);
    }

    private TodoTask(String name, boolean done) {
        super(name, done);
    }

    @Override
    public String toSerialised() {
        return String.format("T | %s | %s", getSerialisedStatus(), getName());
    }

    public static TodoTask fromSerialised(String line) throws CorruptedDataException {
        if (line == null || line.isBlank()) {
            throw new CorruptedDataException("Todo task data is empty");
        }

        String[] parts = line.trim().split("\\s*\\|\\s*", -1);
        if (parts.length != 3 || !parts[0].equals("T")) {
            throw new CorruptedDataException("Malformed todo task data: " + line);
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
                throw new CorruptedDataException("Unknown todo task status: " + parts[1]);
        }

        return new TodoTask(parts[2], done);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
