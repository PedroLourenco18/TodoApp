package br.com.pedrolourenco.todoapp.repository;

import br.com.pedrolourenco.todoapp.domain.task.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository {
    public List<Task> getTasks();
    public List<Task> getTasksByDates(LocalDate startDate, LocalDate endDate);
    public boolean existTitle(String title);
    public void saveTask(Task task);
    public boolean getTaskDone(String title);
}
