package am.task;

import java.time.LocalDateTime;
import java.util.ArrayList;

/** Stores tasks in insertion order and provides list operations. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Appends a task to the end of the list. */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Marks the task at the given zero-based index as done. */
    public void markTask(int taskIndex) {
        tasks.get(taskIndex).mark();
    }

    /** Marks the task at the given zero-based index as not done. */
    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).unmark();
    }

    /** Removes the task at the given zero-based index. */
    public void deleteTask(int taskIndex) {
        tasks.remove(taskIndex);
    }

    /** Returns the task at the given zero-based index. */
    public Task getTask(int taskIndex) {
        return tasks.get(taskIndex);
    }

    public int getLength() {
        return tasks.size();
    }

    /** Returns past scheduled tasks with their original list numbers. */
    public String getPastTasks(LocalDateTime datetime) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isPast(datetime)) {
                continue;
            }
            if (!result.isEmpty()) {
                result.append("\n");
            }
            result.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i));
        }
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                result.append("\n");
            }
            result.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i));
        }
        return result.toString();
    }
}
