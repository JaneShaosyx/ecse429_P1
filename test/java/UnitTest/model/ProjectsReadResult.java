package UnitTest.model;

import java.util.List;

/**
 * Projects read result
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
public class ProjectsReadResult {
    private List<Project> projects;
    private String[] errorMessages;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }
}
