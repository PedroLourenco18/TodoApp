package br.com.pedrolourenco.todoapp.service;

import br.com.pedrolourenco.todoapp.domain.task.Task;
import br.com.pedrolourenco.todoapp.exception.InvalidCommandException;
import br.com.pedrolourenco.todoapp.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public void createTask(String title, String description, LocalDate dueDate){
        if(dueDate.isBefore(LocalDate.now())){
            throw new InvalidCommandException("A data de validade deve ser igual ou posterior à data atual");
        }

        if (title.length() > 30) {
            throw new InvalidCommandException("O Titulo deve ter no maximo 30 caracteres");
        }

        if(taskRepository.existTitle(title)){
            throw new InvalidCommandException("Ja existe uma tarefa com esse titulo");
        }

        Task task = new Task(title, description, dueDate);

        taskRepository.saveTask(task);
    }

    public List<Task> listTasks(){
        return taskRepository.getTasks();
    }

    public List<Task> listTasksByDate(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new InvalidCommandException("Datas Invalidas");
        }
        return taskRepository.getTasksByDates(startDate, endDate);
    }

    public void doTask(String title) {
        if(!taskRepository.getTaskDone(title)){
            throw new InvalidCommandException("Essa tarefa não existe");
        }
    }
}
