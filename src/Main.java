import br.com.pedrolourenco.todoapp.controller.Controller;
import br.com.pedrolourenco.todoapp.repository.TaskRepository;
import br.com.pedrolourenco.todoapp.repository.TaskRepositoryImpl;
import br.com.pedrolourenco.todoapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        TaskRepository taskRepository = new TaskRepositoryImpl(mapper);
        TaskService taskService = new TaskService(taskRepository);
        Controller mainController = new Controller(taskService);
        mainController.start();
    }
}