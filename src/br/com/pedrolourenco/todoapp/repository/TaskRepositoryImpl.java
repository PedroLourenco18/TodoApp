package br.com.pedrolourenco.todoapp.repository;

import br.com.pedrolourenco.todoapp.domain.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository{
    private final List<Task> tasksCache;
    private final ObjectMapper mapper;

    public TaskRepositoryImpl(ObjectMapper mapper) {
        this.mapper = mapper;
        this.tasksCache = this.loadTasks();
    }

    @Override
    public List<Task> getTasks() {
        return List.copyOf(tasksCache);
    }

    @Override
    public List<Task> getTasksByDates(LocalDate startDate, LocalDate endDate) {
        return tasksCache.stream()
                .filter(t ->
                        t.dueDate().isAfter(startDate.minusDays(1)) &&
                                t.dueDate().isBefore(endDate.plusDays(1))
                ).toList();
    }

    @Override
    public boolean existTitle(String title) {
        return tasksCache.stream()
                .map(Task::title)
                .anyMatch(s -> s.equals(title));
    }

    @Override
    public void saveTask(Task task) {
        this.addTaskToCache(task);

        this.persistTasks();
    }

    @Override
    public boolean getTaskDone(String title){
        int taskIndex = -1;

        for (int i = 0; i < tasksCache.size(); i++) {
            if(tasksCache.get(i).title().equals(title)){
                taskIndex = i;
                break;
            }
        }

        if(taskIndex >= 0) {
            this.saveTaskInHistory(tasksCache.get(taskIndex));
            tasksCache.remove(taskIndex);

            persistTasks();

            return true;
        }

        return false;
    }

    private List<Task> loadTasks(){
        try (var in = Files.newBufferedReader(Path.of("data/tasks.json"))){
            return mapper.readValue(in, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addTaskToCache(Task task){
        Comparator<Task> comparator = Comparator.comparing(Task::dueDate);

        int index = Collections.binarySearch(tasksCache, task, comparator);

        if (index < 0) {
            index = -index - 1;
        }

        tasksCache.add(index, task);
    }

    private void persistTasks(){
        try(var out = Files.newBufferedWriter(Path.of("data/tasks.json"))){
            mapper.writeValue(out, tasksCache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveTaskInHistory(Task task){
        try(Writer out = new BufferedWriter(new FileWriter("data/completed_tasks_history.txt", StandardCharsets.UTF_8, true))){
            out.append("Title: " + task.title() +
                    " - Description: " + task.description() +
                    " - Created At: " + task.createdAt() +
                    " - Due Date: " + task.dueDate());
            out.append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
