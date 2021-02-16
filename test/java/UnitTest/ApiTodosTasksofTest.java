package UnitTest;

import UnitTest.model.Project;
import UnitTest.model.ProjectsReadResult;
import UnitTest.model.ProjectsWriteResult;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Test for API: /todos/:id/tasksof
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
@RunWith(RandomTestRunner.class)
public class ApiTodosTasksofTest extends ApiTest {

    /**
     * GET /todos/500/tasksof
     */
    @Test
    public void testGetTodosTasksofId500() throws IOException {
        httpGet("/todos/500/tasksof", ProjectsReadResult.class).assertStatusAndFormed(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * GET /todos/1/tasksof
     */
    @Test
    public void testGetTodosTasksofId1() throws IOException {
        ApiResponse<ProjectsReadResult> response = httpGet("/todos/1/tasksof", ProjectsReadResult.class)
                .assertOkAndFormed();
        Assert.assertEquals(1, response.getBodyParsed().getProjects().size());
        Project project = response.getBodyParsed().getProjects().get(0);
        Assert.assertEquals("Office Work", project.getTitle());
        Assert.assertEquals(false, project.getCompleted());
        Assert.assertEquals(2, project.getTasks().size());
    }

    // ----------------------

    /**
     * HEAD /todos/1/tasksof
     */
    @Test
    public void testHeadTodosTasksofId1() throws IOException {
        httpHead("/todos/1/tasksof").assertOkAndFormed();
    }

    // ----------------------

    private void testPostTodosTasksofWithId(String contentType, String payload) throws IOException {
        ApiResponse<ProjectsWriteResult> response = httpPost("/todos/1/tasksof", contentType, payload, ProjectsWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_NOT_FOUND);
        Assert.assertEquals("Could not find thing matching value for id", response.getBodyParsed().getErrorMessages()[0]);
    }

    /**
     * POST /todos/1/taskof?id=1
     * Content type: json
     */
    @Test
    public void testHeadTodosTasksofId1JSON() throws IOException {
        testPostTodosTasksofWithId(CONTENT_TYPE_JSON, "{\"id\":1}");
    }

    /**
     * POST /todos/1/taskof?id=1
     * Content type: xml
     */
    @Test
    public void testHeadTodosTasksofId1XML() throws IOException {
        testPostTodosTasksofWithId(CONTENT_TYPE_XML, "<project><id>1</id></project>");
    }

    // ----------------------

    private void testPostTodosTasksofWithTitle(String contentType, String payload, String title) throws IOException {
        ApiResponse<ProjectsWriteResult> response = httpPost("/todos/1/tasksof", contentType, payload, ProjectsWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(title, response.getBodyParsed().getTitle());
        deleteTodosTasksof("1", response.getBodyParsed().getId());
    }

    /**
     * POST /todos/1/taskof?title=Title 357
     * Content type: json
     */
    @Test
    public void testPostTodosTasksofWithTitleJSON() throws IOException {
        testPostTodosTasksofWithTitle(CONTENT_TYPE_JSON, "{\"title\":\"Title 357\"}", "Title 357");
    }

    /**
     * POST /todos/1/taskof?title=Title 356
     * Content type: xml
     */
    @Test
    public void testPostTodosTasksofWithTitleXML() throws IOException {
        testPostTodosTasksofWithTitle(CONTENT_TYPE_XML, "<project><title>Title 356</title></project>", "Title 356");
    }

    // ----------------------

    private void testPostTodosTasksofWithCompleted(String contentType, String payload, Boolean completed) throws IOException {
        ApiResponse<ProjectsWriteResult> response = httpPost("/todos/1/tasksof", contentType, payload, ProjectsWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(completed, response.getBodyParsed().getCompleted());
        deleteTodosTasksof("1", response.getBodyParsed().getId());
    }

    /**
     * POST /todos/1/taskof?description=description 357
     * Content type: xml
     */
    @Test
    public void testPostTodosTasksofWithCompletedJSON() throws IOException {
        testPostTodosTasksofWithCompleted(CONTENT_TYPE_JSON, "{\"title\":\"Title 357\", \"completed\":false}", false);
    }

    /**
     * POST /todos/1/taskof?description=description 356
     * Content type: xml
     */
    @Test
    public void testPostTodosTasksofWithCompletedXML() throws IOException {
        testPostTodosTasksofWithCompleted(CONTENT_TYPE_XML, "<project><title>Title 356</title><completed>true</completed></project>", true);
    }


    // ----------------------

    /**
     * DELETE /todos/:id/tasksof/:id
     */
    @Test
    public void testDeleteTodosTasksof() throws IOException {
        ApiResponse<ProjectsWriteResult> postResult = httpPost("/todos/1/tasksof", null, ProjectsWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        ApiResponse<ProjectsReadResult> deleteBefore = httpGet("/todos/1/tasksof", ProjectsReadResult.class).assertOkAndFormed();
        httpDelete("/todos/1/tasksof/" + postResult.getBodyParsed().getId(), void.class).assertStatusAndFormed(HttpStatus.SC_OK);
        httpDelete("/projects/" + postResult.getBodyParsed().getId(), void.class).assertStatusAndFormed(HttpStatus.SC_OK);
        ApiResponse<ProjectsReadResult> deleteAfter = httpGet("/todos/1/tasksof", ProjectsReadResult.class).assertOkAndFormed();
        Assert.assertEquals(1, deleteBefore.getBodyParsed().getProjects().size() - deleteAfter.getBodyParsed().getProjects().size());
    }

    // --------------------------------------

    private void deleteTodosTasksof(String todoId, String id) throws IOException {
        httpDelete("/todos/" + todoId + "/tasksof/" + id, void.class);
        httpDelete("/projects/" + id, void.class);
    }
}
