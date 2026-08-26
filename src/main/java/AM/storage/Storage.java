import Exceptions.CorruptedDataException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public TaskList load() throws IOException, CorruptedDataException {
        TaskList tasks = new TaskList();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        int lineNumber = 0;
        for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
            lineNumber++;
            if (!line.isBlank()) {
                try {
                    tasks.addTask(Task.fromSerialised(line));
                } catch (CorruptedDataException error) {
                    throw new CorruptedDataException("Corrupted task data on line " + lineNumber, error);
                }
            }
        }

        return tasks;
    }

    public void save(TaskList tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < tasks.getLength(); i++) {
            content.append(tasks.getTask(i).toSerialised()).append(System.lineSeparator());
        }

        Files.writeString(filePath, content.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
