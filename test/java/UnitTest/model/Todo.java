package UnitTest.model;


import java.util.List;

/**
 * Todo
 *
 * @author Haohang Xia (created on 2021-02-14)
 * @version 1.0
 */
public class Todo {
    private String id;
    private String title;
    private Boolean doneStatus;
    private String description;
    private List<Task> tasksof;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDoneStatus() {
        return doneStatus;
    }

    public void setDoneStatus(Boolean doneStatus) {
        this.doneStatus = doneStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasksof() {
        return tasksof;
    }

    public void setTasksof(List<Task> tasksof) {
        this.tasksof = tasksof;
    }
}
