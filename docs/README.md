# AM User Guide

## Editing task details

Use the same text command in the console or GUI input box:

```text
edit INDEX [/name DESCRIPTION] [/by VALUE] [/from VALUE] [/to VALUE]
```

Supply at least one field. Fields may appear in any order, once each.
`INDEX` is the current one-based task-list number, including the numbers shown
by `find` and `past`. Old GUI messages are not refreshed after changes.

| Task | Editable fields |
| --- | --- |
| Todo | `/name` |
| Deadline | `/name`, `/by` |
| Event | `/name`, `/from`, `/to` |

Editing preserves omitted fields, task type, completion status, list position,
and task count. Completed and past tasks can be edited. Duplicate tasks are
allowed. Use `mark` and `unmark` for completion; editing does not convert types
or reorder tasks.

### Examples

Suppose task 3 is this completed event:

```text
3. [E][X] project meeting (from: Sep 10 2026 2:00 PM to: Sep 10 2026 4:00 PM)
```

Change only its end time:

```text
edit 3 /to 1800
```

Response:

```text
Edited:
3. [E][X] project meeting (from: Sep 10 2026 2:00 PM to: Sep 10 2026 6:00 PM)
```

Other valid examples:

```text
edit 1 /name buy milk
edit 2 /by 2026-09-12
edit 3 /to 2026-09-12 1700 /name planning session /from 2026-09-12 1500
```

The last command validates the final interval after applying both endpoints.
End must be at or after start; equal endpoints are allowed. Editing one endpoint
never adjusts the other automatically.

### Dates and descriptions

| Value | Meaning |
| --- | --- |
| `2026-09-12 1800` | Replace date and time |
| `2026-09-12` | Replace date; use midnight for start, 23:59 for end/deadline |
| `1800` | Preserve that field's existing date and replace its time |

Time-only values require four digits in 24-hour format, from `0000` to `2359`.
They never roll into another day. Omitted timestamps retain any stored seconds;
explicit times use zero seconds. Time-only input is available for editing only.

Descriptions must be nonblank and cannot contain `|` or line breaks. Outer
whitespace is trimmed and internal spacing is preserved. Slash-prefixed tokens
are reserved for markers: `/name read/write` works; `/name inspect /tmp` does
not. Quoting and escaping are not supported. Commands and markers are
case-sensitive. Leading and trailing whitespace is accepted, multiple spaces between
arguments are allowed, and blank commands are ignored. Description spacing is preserved.

### Invalid examples and responses

These examples assume there are three tasks, with task 1 a todo and task 3
an event starting at 14:00.

The GUI adds an `Error` heading above each error response. Syntax errors use the
same explanation and an example on the next line:

```text
You messed up the command.
Example: edit 1 /name buy milk
```

This response applies to missing fields or values, repeated or unknown markers,
unsupported fields for the task type, invalid descriptions, and malformed task
numbers. For example: `edit 3`, `edit 3 /name`, `edit 3 /to 1700 /to 1800`,
`edit 3 /until 1800`, and `edit 1 /by 2026-09-12` all receive that response.

Date and missing-task errors retain their own messages:

| Input | Exact response body |
| --- | --- |
| `edit 3 /to 2400` | `When is that?` |
| `edit 3 /to 900` | `When is that?` |
| `edit 3 /to 2026-02-30` | `When is that?` |
| `edit 3 /to 1300` | `When is that?` |
| `edit 4 /name missing` | `Task 4 does not exist. Choose a task number from 1 to 3. Use list to see your tasks.` |

An unchanged result returns `No changes.` without saving. Rejected edits change
neither the task list nor the file. Responses retain the console's existing
separators and indentation; the GUI displays a new response bubble. Dates use
the existing locale-dependent display format; examples here use English.

### Saving and compatibility

Successful edits call the existing save operation immediately, replacing the
whole contents of `./data/AM.txt`. The format and numbering remain unchanged:

```text
T | 0 | buy milk
D | 0 | submit report | 2026-09-12T23:59
E | 1 | project meeting | 2026-09-10T14:00 | 2026-09-10T18:00
```

`0` means incomplete and `1` means complete. File order determines numbering.
No IDs, additional fields, or migrations are introduced.

A failed save returns `I couldn't access my memory.` and leaves the live task
list unchanged. As with existing saves, a write failure can leave a partially
written file; there is no atomic replacement or disk rollback.

New task descriptions also reject the pipe character (`|`). Loading keeps its
existing behavior. An edit validates the
resulting task, so a previously stored invalid description or event interval
must be repaired in the same edit. Loading validation is not tightened.

## Command input and validation

When `list`, `past`, or `find` has no results, AM replies `I don't see anything`.
This is a normal response, so the GUI clears the submitted command.

Blank input is ignored. Leading/trailing whitespace and multiple spaces between
arguments are accepted. Spacing within descriptions and embedded slashes such as
`read/write` are preserved. Descriptions cannot contain `|` or line breaks;
search keywords are not restricted this way.

Deadline, event, and edit parameters must use supported markers, each at most once,
with a nonblank value. Unknown parameters are rejected. Both `/from` and `/to` are
required when creating an event; their order does not matter.

All syntax errors for a recognized command start with `You messed up the command.`
and show `Example: ` followed by the corresponding command below on a new line:

| Command | Example |
| --- | --- |
| `bye` | `bye` |
| `list` | `list` |
| `past` | `past` |
| `find` | `find milk` |
| `todo` | `todo buy milk` |
| `deadline` | `deadline report /by 2026-09-12 1800` |
| `event` | `event meeting /from 2026-09-12 1400 /to 2026-09-12 1600` |
| `edit` | `edit 1 /name buy milk` |
| `mark` | `mark 1` |
| `unmark` | `unmark 1` |
| `delete` | `delete 1` |

This includes missing descriptions, keywords, task numbers, markers, and values;
repeated or unsupported parameters; forbidden description characters; and extra
arguments to `bye`, `list`, or `past`.

Task numbers must be positive whole numbers no greater than 2147483647. Missing,
non-integer, oversized, and non-positive numbers receive the common syntax message.
A positive number absent from the current list instead produces a message with the
current range, or explains that the list is empty.

Commands and markers remain case-sensitive. Unknown commands, invalid dates and
times, missing tasks, and storage failures retain their separate error responses.
