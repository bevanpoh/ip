package am.task;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Maintains the ordered collection of tasks used by the application.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Marks the task at an index as complete.
     *
     * @param taskIndex zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void markTask(int taskIndex) {
        tasks.get(taskIndex).mark();
    }

    /**
     * Marks the task at an index as incomplete.
     *
     * @param taskIndex zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).unmark();
    }

    /**
     * Removes the task at an index.
     *
     * @param taskIndex zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void deleteTask(int taskIndex) {
        tasks.remove(taskIndex);
    }

    /**
     * Returns the task at an index.
     *
     * @param taskIndex zero-based task index
     * @return task at the requested index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task getTask(int taskIndex) {
        return tasks.get(taskIndex);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int getLength() {
        return tasks.size();
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword text to find in task descriptions
     * @return matching tasks in their original order, or an empty string
     */
    public String getMatchingTask(String keyword) {
        String normalizedKeyword = keyword.trim().toLowerCase();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getName().toLowerCase().contains(normalizedKeyword)) {
                if (!result.isEmpty()) {
                    result.append("\n");
                }
                result.append(i + 1)
                        .append(". ")
                        .append(task);
            }
        }
        return result.toString();
    }

    /**
     * Formats tasks that ended before a specified time.
     *
     * @param datetime time used to identify past tasks
     * @return numbered display text for past tasks
     */
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

    /**
     * Formats every task in list order with one-based display numbering.
     *
     * @return numbered display text for all tasks
     */
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
