package am.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import am.task.DeadlineTask;
import am.task.EventTask;
import am.task.TodoTask;

/**
 * Tests the translation from user input into command objects.
 *
 * <p>These tests deliberately inspect the command data, rather than running the
 * whole application, so a failing test points directly to a parsing problem.</p>
 */
public class CommandParserTest {
    @Test
    void parsesCommandsWithoutArguments() {
        assertInstanceOf(Command.ByeCommand.class, CommandParser.parse("bye"));
        assertInstanceOf(Command.ListCommand.class, CommandParser.parse("list"));
        assertInstanceOf(Command.PastCommand.class, CommandParser.parse("past"));
    }

    @Test
    void parsesTaskIndexesAsZeroBasedIndexes() {
        Command.MarkCommand mark = assertInstanceOf(
                Command.MarkCommand.class, CommandParser.parse("mark 2"));
        Command.UnmarkCommand unmark = assertInstanceOf(
                Command.UnmarkCommand.class, CommandParser.parse("unmark 2"));
        Command.DeleteTaskCommand delete = assertInstanceOf(
                Command.DeleteTaskCommand.class, CommandParser.parse("delete 2"));

        assertEquals(1, mark.getIndex());
        assertEquals(1, unmark.getIndex());
        assertEquals(1, delete.getIndex());
    }

    @Test
    void parsesTodoAndTrimsItsName() {
        Command.AddTaskCommand command = assertInstanceOf(
                Command.AddTaskCommand.class,
                CommandParser.parse("todo   buy bread   "));

        TodoTask task = assertInstanceOf(TodoTask.class, command.getTask());
        assertEquals("[T][ ] buy bread", task.toString());
    }

    @Test
    void parsesDeadlineWithDateOnly() {
        Command.AddTaskCommand command = assertInstanceOf(
                Command.AddTaskCommand.class,
                CommandParser.parse("deadline submit report /by 2026-08-28"));

        DeadlineTask task = assertInstanceOf(DeadlineTask.class, command.getTask());
        assertEquals("[D][ ] submit report (by: Aug 28 2026 11:59 PM)", task.toString());
    }

    @Test
    void parsesEventWithDateAndTimeParameters() {
        Command.AddTaskCommand command = assertInstanceOf(
                Command.AddTaskCommand.class,
                CommandParser.parse("event project meeting /from 2026-08-28 1400 /to 2026-08-28 1600"));

        EventTask task = assertInstanceOf(EventTask.class, command.getTask());
        assertEquals(
                "[E][ ] project meeting (from: Aug 28 2026 2:00 PM to: Aug 28 2026 4:00 PM)",
                task.toString());
    }

    @Test
    void rejectsMissingArguments() {
        assertInvalid("mark");
        assertInvalid("unmark");
        assertInvalid("delete");
        assertInvalid("todo");
        assertInvalid("deadline");
        assertInvalid("event");
    }

    @Test
    void rejectsNonNumericTaskIndexes() {
        assertInvalid("mark abc");
        assertInvalid("unmark 1.5");
        assertInvalid("delete two");
    }

    @Test
    void rejectsMissingOrRepeatedParameters() {
        assertInvalid("deadline report");
        assertInvalid("deadline report /by");
        assertInvalid("event meeting /from 2026-08-28");
        assertInvalid("event meeting /from 2026-08-28 /to");
        assertInvalid("deadline report /by 2026-08-28 /by 2026-08-29");
        assertInvalid("event meeting /from 2026-08-28 /to 2026-08-28 1600 /to 2026-08-28 1700");
    }

    @Test
    void rejectsInvalidDates() {
        InvalidCommandException exception = assertThrows(
                InvalidCommandException.class,
                () -> CommandParser.parse("deadline report /by Sunday"));

        assertEquals("When is that?", exception.getMessage());
    }

    @Test
    void rejectsUnknownAndCaseSensitiveCommands() {
        assertThrows(UnknownCommandException.class, () -> CommandParser.parse("wat"));
        assertThrows(UnknownCommandException.class, () -> CommandParser.parse("LIST"));
        assertThrows(UnknownCommandException.class, () -> CommandParser.parse(" list"));
        assertThrows(UnknownCommandException.class, () -> CommandParser.parse(""));
    }

    /**
     * Verifies the common error type and message for malformed commands.
     *
     * @param input malformed command text
     */
    private static void assertInvalid(String input) {
        InvalidCommandException exception = assertThrows(
                InvalidCommandException.class, () -> CommandParser.parse(input));
        assertEquals("You messed up the command.", exception.getMessage());
    }

}
