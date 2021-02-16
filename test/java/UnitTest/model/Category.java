package UnitTest.model;

/**
 * Category.
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
public class Category {
    private String id;
    private String title;
    private Boolean doneStatus;
    private String description;

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
}
