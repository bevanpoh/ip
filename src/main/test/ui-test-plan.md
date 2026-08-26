# AM CLI Test Plan

## 1. Purpose

This document defines repeatable tests for the task-management CLI. The tests are intended to verify the behaviours shown in the requirements examples and to detect regressions when commands, parsing, task formatting, or list operations change.

The tests use a fresh application process for each scenario. This is important because tasks are stored in memory and a process that is reused would carry state from one test into the next.

## 2. Test conventions

- Send commands one per line through standard input.
- End every scenario with `bye` so that the application terminates cleanly.
- Task numbers in user commands are one-based. Internally, the corresponding list index is zero-based.
- Each expected list line must preserve order, task type (`[T]`, `[D]`, or `[E]`), status (`[ ]` or `[X]`), task description, and any deadline/event details.
- Unless a test says otherwise, an invalid command must not change the task list.

## 3. Concrete UI test commands and expected outputs

The following are black-box tests. Start a fresh process for each case, provide the commands exactly as shown, and compare the response text between separator lines with the expected output. The expected-output blocks show semantic response bodies, while the full-console checks in UI-00 verify that the startup banner, separator lines, and four-space indentation are still present. The expected output below follows the current implementation, including its response labels and spacing, except where a test explicitly defines intended rejection of malformed input.

### UI-00: Verify the full console envelope

Commands:

```text
bye
```

Expected full-console structure:

```text
<separator line>
<the seven-line AM banner>
My name is AM.
What do you want?
<separator line>
<separator line>
You may leave, but I will be here.
<separator line>
```

The exact banner characters, separator characters, and four-space indentation must be preserved. The process must exit successfully after the farewell response. This is a formatting check on the complete console output, not only on the response body.

### UI-01: List an empty task list

Commands:

```text
list
bye
```

Expected output from `list`:

```text
<empty response body>
```

Expected output from `bye`:

```text
You may leave, but I will be here.
```

The process must then exit successfully.

### UI-02: Add and list a todo task

Commands:

```text
todo buy bread
list
bye
```

Expected output from `todo buy bread`:

```text
added: [T][ ] buy bread
Now you have 1 tasks in the list
```

Expected output from `list`:

```text
1. [T][ ] buy bread
```

### UI-03: Mark and unmark a task

Commands:

```text
todo return book
mark 1
unmark 1
list
bye
```

Expected output from `mark 1`:

```text
Marked:
[T][X] return book
```

Expected output from `unmark 1`:

```text
Unmarked:
[T][ ] return book
```

Expected output from the final `list`:

```text
1. [T][ ] return book
```

### UI-04: Add deadline and event tasks

Commands:

```text
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output from the deadline command:

```text
added: [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list
```

Expected output from the event command:

```text
added: [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list
```

Expected output from `list`:

```text
1. [D][ ] return book (by: Sunday)
2. [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

### UI-05: Delete a task and renumber the remaining tasks

Commands:

```text
todo read book
todo return book
todo buy bread
delete 2
list
bye
```

Expected output from `delete 2`:

```text
Deleted:
[T][ ] return book
Now you have 2 tasks in the list
```

Expected output from `list`:

```text
1. [T][ ] read book
2. [T][ ] buy bread
```

The deleted task must not appear, and the remaining tasks must be numbered consecutively.

### UI-06: Run a complete mixed-task workflow

Commands:

```text
todo read book
todo return book
todo buy bread
mark 1
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
unmark 1
delete 3
list
bye
```

Expected output from the final `list`:

```text
1. [T][ ] read book
2. [T][ ] return book
3. [T][ ] borrow book
4. [D][ ] return book (by: Sunday)
5. [E][ ] project meeting (from: Mon 2pm to: 4pm)
```

This workflow verifies insertion order, all three task formats, mark/unmark state changes, deletion, and renumbering. The final count must be 5.

### UI-07: Reject invalid task numbers without changing the list

Commands:

```text
todo read book
mark 2
unmark 0
delete 3
list
bye
```

Expected output for the invalid task-number commands:

```text
You don't have task number 2
You don't have task number 0
You don't have task number 3
```

Expected output from `list`:

```text
1. [T][ ] read book
```

The invalid operations must not mark, unmark, delete, or duplicate the task.

### UI-08: Reject an unknown command and continue

Commands:

```text
wat
list
bye
```

Expected output from `wat`:

```text
AAAAHHHHHHHHHHHHHHH
You can't tell me to 'wat'
```

Expected output from `list`:

```text
<empty response body>
```

The application must continue running after the error and must exit only after `bye`.

### UI-09: Reject repeated structured-task separators

Commands:

```text
deadline duplicate /by Sunday /by Monday
event repeated /from Mon /to 4pm /to 5pm
list
bye
```

Expected output from each malformed structured command:

```text
You messed up the command.
```

Expected output from `list`:

```text
<empty response body>
```

The parser must reject repeated `/by`, `/from`, or `/to` separators instead of preserving extra text in a deadline or silently discarding an event field.

### UI-10: Define whitespace and command-case behaviour

Commands:

```text
  list
LIST

list
bye
```

The current command contract is strict: leading whitespace, different command case, and an empty line are treated as unknown commands. Under that contract, the first three inputs each produce:

```text
AAAAHHHHHHHHHHHHHHH
You can't tell me to '<input as entered>'
```

The later `list` command must still run and produce an empty response body. If the project later decides to normalize leading whitespace, blank input, or command case, update this test's expected output and parser requirements together.

## 4. Acceptance test scenarios

| ID | Priority | Input sequence | Expected result |
| --- | --- | --- | --- |
| CLI-01 | High | `list`, `bye` | The list response is empty for a new process. No task is displayed and the program exits after `bye`. |
| CLI-02 | High | `todo buy bread`, `list`, `bye` | A todo task is added. The confirmation identifies `[T][ ] buy bread`, reports `Now you have 1 tasks in the list`, and `list` shows it as item 1 with the current `1. ` spacing. |
| CLI-03 | High | `todo read book`, `todo return book`, `todo buy bread`, `list`, `bye` | Three tasks appear in insertion order as items 1–3, each with `[T][ ]`. The reported count is 3. |
| CLI-04 | High | `todo return book`, `mark 1`, `list`, `bye` | The `Marked:` response shows `[T][X] return book`; the subsequent list also shows the task as done. |
| CLI-05 | High | `todo return book`, `mark 1`, `unmark 1`, `list`, `bye` | The task changes back to `[T][ ] return book`. It is not removed or duplicated. |
| CLI-06 | High | `todo return book`, `deadline return book /by Sunday`, `list`, `bye` | The deadline task is added as `[D][ ] return book (by: Sunday)`. The deadline text is preserved exactly after trimming surrounding whitespace. |
| CLI-07 | High | `event project meeting /from Mon 2pm /to 4pm`, `list`, `bye` | The event task is added as `[E][ ] project meeting (from: Mon 2pm to: 4pm)`. Both time fields are preserved and displayed in the correct positions. |
| CLI-08 | High | `todo read book`, `todo return book`, `todo buy bread`, `delete 2`, `list`, `bye` | The `Deleted:` response identifies `return book`, reports `Now you have 2 tasks in the list`, and `list` shows `read book` as item 1 and `buy bread` as item 2. No stale item 3 remains. |
| CLI-09 | High | Add three tasks, then `mark 2`, `unmark 2`, `delete 1`, `list`, `bye` | Every operation affects the requested one-based item. After deletion, the remaining task is renumbered from item 2 to item 1, and its status is unchanged. |
| CLI-10 | High | `todo read book`, `mark 2`, `unmark 0`, `delete 3`, `list`, `bye` | Each invalid index produces an error identifying the requested task number. The original task remains present and not done. |
| CLI-11 | High | `mark`, `unmark`, `delete`, `todo`, `deadline return book`, `event meeting`, `bye` | Each command missing a required argument produces the missing-argument error. No malformed task is added and the program continues accepting commands. |
| CLI-12 | High | `deadline return book /by Sunday`, `event meeting /from Mon 2pm`, `event meeting /to 4pm`, `list`, `bye` | Each malformed deadline/event command is rejected. No partial deadline/event task is added. |
| CLI-13 | Medium | `wat`, `list`, `bye` | The unknown-command error identifies the unrecognised input. The application remains alive and the list is still empty. |
| CLI-14 | Medium | `todo read book`, `mark 1`, `mark 1`, `unmark 1`, `unmark 1`, `list`, `bye` | Repeating mark/unmark is safe and idempotent. The final task is shown once as `[T][ ] read book`. |
| CLI-15 | Medium | `todo read book`, `todo return book`, `mark 1`, `deadline return book /by Sunday`, `event project meeting /from Mon 2pm /to 4pm`, `list`, `bye` | The mixed list contains the correct three task types, preserves insertion order, and shows only item 1 as done. |
| CLI-16 | High | Run the complete transcript in Section 5 in a fresh process | Every intermediate list, current response label, count, status transition, type marker, deadline/event detail, and final deletion agrees with the actual UI output documented in Section 3. |
| CLI-17 | High | `deadline duplicate /by Sunday /by Monday`, `event repeated /from Mon /to 4pm /to 5pm`, `list`, `bye` | Both malformed commands are rejected, the list remains empty, and no structured-task data is lost or silently ignored. |
| CLI-18 | Medium | `bye` in a fresh process | The complete startup banner, separator lines, indentation, farewell response, and successful process exit are present. |

## 5. End-to-end regression scenario based on the examples

Run the following as one input script. The expected state is stated after each command so that a failure can be localized.

```text
todo read book
todo return book
todo buy bread
list
mark 2
unmark 2
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
delete 3
list
bye
```

Expected semantic checkpoints:

1. After the first three commands, the list has three not-done todo tasks in insertion order.
2. `list` displays `read book`, `return book`, and `buy bread` as items 1–3.
3. `mark 2` changes only `return book` to done.
4. `unmark 2` changes only `return book` back to not done.
5. `todo borrow book` adds a fourth task and reports the new count.
6. The deadline and event commands add correctly formatted `[D]` and `[E]` tasks, including their details.
7. The next `list` contains six tasks in insertion order, with all tasks not done.
8. `delete 3` removes `buy bread`, reports the removed task, and reports five remaining tasks.
9. The final `list` contains five tasks and renumbers the later tasks consecutively.
10. `bye` prints the farewell response and exits with a successful process status.

The examples in the requirements show some pre-existing tasks and counts that are not created in the visible command sequence. For an automated test, use the explicit fixture above and assert the resulting count from that fixture rather than relying on those illustrative counts.

## 6. Parser/unit test matrix

These checks can be implemented against `CommandParser.parse` without launching the CLI.

### Valid commands

| Input | Expected parsed command/data |
| --- | --- |
| `bye` | `ByeCommand` |
| `list` | `ListCommand` |
| `mark 2` | `MarkCommand` with internal index 1 |
| `unmark 2` | `UnmarkCommand` with internal index 1 |
| `delete 2` | `DeleteTaskCommand` with internal index 1 |
| `todo borrow book` | `AddTaskCommand` containing a todo named `borrow book` |
| `deadline return book /by Sunday` | `AddTaskCommand` containing the name and deadline separately |
| `event project meeting /from Mon 2pm /to 4pm` | `AddTaskCommand` containing the name, start, and end separately |

### Invalid or boundary inputs

Verify that the parser rejects or handles each case according to the intended contract:

- missing arguments: `mark`, `unmark`, `delete`, `todo`, `deadline`, `event`;
- non-numeric indices: `mark abc`, `unmark 1.5`, `delete two`;
- malformed structured commands: missing `/by`, `/from`, or `/to`;
- extra separators or repeated separators in deadline/event commands must be rejected without creating a task;
- leading whitespace, trailing whitespace, empty input, and multiple spaces between command parts must follow the explicit command-normalization contract;
- empty input;
- unknown command names and command names with different case, if commands are intended to be case-sensitive;
- index `0`, negative indices, and very large indices.

For every rejected input, assert the exception type and verify that the application layer leaves the task list unchanged. For every accepted input, assert the command type and all parsed fields, not just the type.

## 7. Task and `TaskList` unit tests

- A new `Task`, `TodoTask`, `DeadlineTask`, and `EventTask` is not done by default.
- `mark()` changes status to done; `unmark()` changes it to not done.
- Each task's `toString()` contains the correct type marker, status marker, name, and structured details.
- A new `TaskList` has length 0 and an empty string representation.
- `addTask` appends and increases length by one.
- `getTask` returns tasks in insertion order.
- `markTask` and `unmarkTask` affect only the selected task.
- `deleteTask` removes the selected task, decreases length by one, and shifts later tasks left.
- Accessing an invalid list index fails predictably; the CLI should convert that failure into its user-facing error rather than terminating.

## 8. Test execution and evidence

Use Java 25 for compilation and execution. Keep the input script, captured output, exit status, and test result together as evidence for each acceptance run. A regression run should include all High-priority cases and the complete transcript scenario; Medium-priority cases should run whenever parsing or user-facing messages change.

For failures, record:

1. test ID;
2. exact input;
3. expected semantic output;
4. actual output;
5. whether the task list changed unexpectedly;
6. the commit or revision being assessed.

## 9. Assessment rule

The CLI passes this plan only if all High-priority tests pass, including the full-console envelope and repeated-separator checks, the complete transcript scenario passes, and no test reveals data loss, incorrect numbering, cross-task mutation, malformed structured-task output, or an uncaught error that terminates the application unexpectedly.
