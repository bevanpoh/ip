package AM.task;

import AM.storage.CorruptedDataException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/** Tests serialization and deserialization of {@link EventTask}. */
public class EventTaskTest {
    @Test
    void serialisesAndDeserialisesEventTask() throws CorruptedDataException {
        EventTask original = new EventTask(
                "project meeting",
                LocalDateTime.of(2026, 8, 28, 14, 0),
                LocalDateTime.of(2026, 8, 28, 16, 0));
        original.mark();

        String serialised = original.toSerialised();
        assertEquals("E | 1 | project meeting | 2026-08-28T14:00 | 2026-08-28T16:00", serialised);

        EventTask restored = assertInstanceOf(EventTask.class, Task.fromSerialised(serialised));
        assertEquals(original.toString(), restored.toString());
        assertEquals(serialised, restored.toSerialised());
    }
}
