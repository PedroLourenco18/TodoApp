package br.com.pedrolourenco.todoapp.controller.terminal;

public enum SystemMessages {
    INITIAL_MESSAGE("Bem vindo ao gerenciador de tarefas!"),
    STARTER_GUIDE("Comandos: \n create: criar nova tarefa \n " +
            "list: lista todas as tarefas \n " +
            "list *dd/MM/yyy* *dd/MM/yyyy*: lista as tarefas que vencem entre as duas datas \n " +
            "done *titulo da tarefa*: marca a tarefa como concluida \n " +
            "exit: fecha o gerenciador de tarefas"),
    INVALID_COMMAND("Por favor digite um comando valido");

    private final String message;

    SystemMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
