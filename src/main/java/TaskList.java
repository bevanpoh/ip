import Exceptions.TaskListLengthExceededException;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void markTask(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= tasks.size() || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        tasks.get(taskIdx).mark();
    }

    public void unmarkTask(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= tasks.size() || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        tasks.get(taskIdx).unmark();
    }

    public void deleteTask(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= tasks.size() || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        tasks.remove(taskIdx);
    }

    public String getTaskString(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= tasks.size() || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        return tasks.get(taskIdx).toString();
    }

    public int getLength() {
        return tasks.size();
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
