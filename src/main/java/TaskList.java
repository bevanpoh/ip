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

    public void markTask(int taskNum) {
        tasks[taskNum].mark();
    }

    public void unmarkTask(int taskNum) {
        tasks[taskNum].unmark();
    }

    public String getTaskString(int taskNum) {
        return tasks[taskNum].toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < task_count; i++) {
            if (i > 0) {
                result.append("\n");
            }
            result.append(i+1)
                    .append(". ")
                    .append(tasks[i]);
        }
        return result.toString();
    }
}
