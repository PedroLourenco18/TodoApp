package br.com.pedrolourenco.todoapp.controller;

import br.com.pedrolourenco.todoapp.domain.task.Task;
import br.com.pedrolourenco.todoapp.controller.terminal.SystemMessages;
import br.com.pedrolourenco.todoapp.controller.terminal.UserCommands;
import br.com.pedrolourenco.todoapp.exception.InvalidCommandException;
import br.com.pedrolourenco.todoapp.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private final TaskService taskService;

    private final Scanner scanner = new Scanner(System.in);

    public Controller(TaskService taskService) {
        this.taskService = taskService;
    }

    public void start(){
        System.out.println(SystemMessages.INITIAL_MESSAGE.getMessage());
        System.out.println(SystemMessages.STARTER_GUIDE.getMessage());

        boolean exit = false;
        while(!exit){
            System.out.println("Digite o comando: ");
            String command = scanner.nextLine();

            String[] commandParts = command.split(" ");

            //EXIT
            if (command.equals(UserCommands.EXIT)) {
                System.out.println("Obrigado por usar o Gerenciador de Tarefas");
                exit = true;
            }

            try {
                //LIST
                if (commandParts[0].equals(UserCommands.LIST)) {
                    List<LocalDate> dateList;

                    dateList = getDates(commandParts);

                    List<Task> tasks;
                    if (dateList.isEmpty()) {
                        tasks = taskService.listTasks();
                    }else if(dateList.size() == 2){
                        tasks = taskService.listTasksByDate(dateList.get(0), dateList.get(1));
                    }else{
                        throw new InvalidCommandException("Insira duas datas");
                    }

                    if(tasks.isEmpty()){
                        System.out.println("Não existem tarefas entre as datas fornecidas");
                        continue;
                    }

                    System.out.println(taskListToString(tasks));
                    continue;
                }

                //CREATE
                if (command.equals(UserCommands.CREATE)) {
                    String taskTitle = this.getTaskInfo();
                    System.out.println("Tarefa " + taskTitle + " criada");
                    continue;
                }

                //DO TASK
                if (commandParts[0].equals(UserCommands.DO_TASK)) {
                    String title = command.substring(UserCommands.DO_TASK.length() + 1);

                    taskService.doTask(title);
                    System.out.println("Tarefa " + title + " concluida");
                    continue;
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (Exception e){
                System.out.println("Houve um problema interno");
                exit = true;
            }

            System.out.println(SystemMessages.INVALID_COMMAND.getMessage());
        }
    }

    private List<LocalDate> getDates(String[] commandParts){
        try {
            return Arrays.stream(commandParts, 1, commandParts.length)
                    .map(s -> LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .toList();
        } catch (Exception e) {
            throw new InvalidCommandException("Datas Invalidas");
        }
    }

    private String getTaskInfo() {
        System.out.println("Digite o titulo (até 30 caracteres): ");
        String title = scanner.nextLine();
        if (title.length() > 30) {
            throw new InvalidCommandException("O Titulo deve ter no maximo 30 caracteres");
        }

        System.out.println("Digite a descrição: ");
        String description = scanner.nextLine();

        System.out.println("Digite a data de vencimento(dd/MM/yyyy): ");
        String dateTyped = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateTyped, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new InvalidCommandException("Data Invalida");
        }

        taskService.createTask(title, description, date);

        return title;
    }

    private String taskListToString(List<Task> taskList){
        return taskList.stream()
                .map(t -> {
                    StringBuilder taskInfo = new StringBuilder();
                    taskInfo.append("Titulo: ").append(t.title())
                            .append("\nDescrição: ").append(t.description())
                            .append("\nData de criação: ").append(t.createdAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .append("\nData de vencimento: ").append(t.dueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    return taskInfo.toString();
                })
                .reduce(
                        "Tarefas a serem feitas:",
                        (s, s2) -> s + "\n--------------------\n" + s2
                ).concat("\n--------------------\n");
    }
}
