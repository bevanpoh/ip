package AM.task;

import AM.storage.CorruptedDataException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/** Tests serialization and deserialization of {@link DeadlineTask}. */
public class DeadlineTaskTest {
    @Test
    void serialisesAndDeserialisesDeadlineTask() throws CorruptedDataException {
        DeadlineTask original = new DeadlineTask(
                "submit report", LocalDateTime.of(2026, 8, 28, 23, 59));

        String serialised = original.toSerialised();
        assertEquals("D | 0 | submit report | 2026-08-28T23:59", serialised);

        DeadlineTask restored = assertInstanceOf(
                DeadlineTask.class, Task.fromSerialised(serialised));
        assertEquals(original.toString(), restored.toString());
        assertEquals(serialised, restored.toSerialised());
    }
}
