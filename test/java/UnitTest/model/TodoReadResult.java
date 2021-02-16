package UnitTest.model;


import java.util.List;

/**
 * Todos read result
 *
 * @author Haohang Xia (created on 2021-02-14)
 * @version 1.0
 */
public class TodoReadResult {
    private List<Todo> todos;
    private String[] errorMessages;

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }
}
