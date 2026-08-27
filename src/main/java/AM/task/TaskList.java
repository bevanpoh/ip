package AM.task;

import java.util.ArrayList;
import java.time.LocalDateTime;

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
     * @param taskIdx zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void markTask(int taskIdx) {
        tasks.get(taskIdx).mark();
    }

    /**
     * Marks the task at an index as incomplete.
     *
     * @param taskIdx zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void unmarkTask(int taskIdx) {
        tasks.get(taskIdx).unmark();
    }

    /**
     * Removes the task at an index.
     *
     * @param taskIdx zero-based task index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void deleteTask(int taskIdx) {
        tasks.remove(taskIdx);
    }

    /**
     * Returns the task at an index.
     *
     * @param taskIdx zero-based task index
     * @return task at the requested index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task getTask(int taskIdx) {
        return tasks.get(taskIdx);
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
