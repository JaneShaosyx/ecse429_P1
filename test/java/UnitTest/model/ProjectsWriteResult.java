package UnitTest.model;

/**
 * Categories write result
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
public class ProjectsWriteResult extends Project {
    private String[] errorMessages;

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }
}
