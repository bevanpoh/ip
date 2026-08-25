import Exceptions.TaskListLengthExceededException;

public class TaskList {
    private final Task[] tasks;
    private int task_count;

    public TaskList(int size) {
        tasks = new Task[size];
        task_count = 0;
    }

    public void addTask(Task task) {
        tasks[task_count++] = task;
    }

    public void markTask(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= task_count || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        tasks[taskIdx].mark();
    }

    public void unmarkTask(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= task_count || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        tasks[taskIdx].unmark();
    }

    public String getTaskString(int taskIdx) throws TaskListLengthExceededException {
        if (taskIdx >= task_count || taskIdx < 0) {
            throw new TaskListLengthExceededException(String.format("Item index '%d' is out of bounds", taskIdx));
        }
        return tasks[taskIdx].toString();
    }

    public int getLength() {
        return task_count;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < task_count; i++) {
            if (i > 0) {
                result.append("\n");
            }
            result.append(i + 1)
                    .append(". ")
                    .append(tasks[i]);
        }
        return result.toString();
    }
}
