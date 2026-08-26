package AM.task;

import AM.storage.CorruptedDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/** Tests serialization and deserialization of {@link TodoTask}. */
public class TodoTaskTest {
    @Test
    void serialisesAndDeserialisesTodoTask() throws CorruptedDataException {
        TodoTask original = new TodoTask("buy bread");
        original.mark();

        String serialised = original.toSerialised();
        assertEquals("T | 1 | buy bread", serialised);

        TodoTask restored = assertInstanceOf(TodoTask.class, Task.fromSerialised(serialised));
        assertEquals(original.toString(), restored.toString());
        assertEquals(serialised, restored.toSerialised());
    }
}
