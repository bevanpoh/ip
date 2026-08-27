package AM.task;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void markTask(int taskIdx) {
        tasks.get(taskIdx).mark();
    }

    public void unmarkTask(int taskIdx) {
        tasks.get(taskIdx).unmark();
    }

    public void deleteTask(int taskIdx) {
        tasks.remove(taskIdx);
    }

    public Task getTask(int taskIdx) {
        return tasks.get(taskIdx);
    }

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
