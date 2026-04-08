package br.com.pedrolourenco.todoapp.repository;

import br.com.pedrolourenco.todoapp.domain.task.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository {
    List<Task> getTasks();
    List<Task> getTasksByDates(LocalDate startDate, LocalDate endDate);
    boolean existTitle(String title);
    void saveTask(Task task);
    boolean getTaskDone(String title);
}
