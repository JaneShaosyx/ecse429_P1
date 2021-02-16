package UnitTest;

import UnitTest.model.Todo;
import UnitTest.model.TodoReadResult;
import UnitTest.model.TodoWriteResult;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Test for API: /todos
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
@RunWith(RandomTestRunner.class)
public class ApiTodosTest extends ApiTest {

    // ----------------------

    /**
     * GET /todos
     */
    @Test
    public void testGetTodos() throws IOException {
        ApiResponse<TodoReadResult> response = httpGet("/todos", TodoReadResult.class)
                .assertOkAndFormed();
        TodoReadResult todos = response.getBodyParsed();
        Assert.assertNotNull("Response is null", todos);
        Assert.assertEquals("Elements count is not 2", 2, todos.getTodos().size());
        Assert.assertAnyEquals("[0]Title invalid", new String[]{"scan paperwork", "file paperwork"}, todos.getTodos().get(0).getTitle());
        Assert.assertEquals("[1]DoneStatus invalid", false, todos.getTodos().get(1).getDoneStatus());
        Assert.assertEquals("[1]Description invalid", "", todos.getTodos().get(1).getDescription());
    }

    /**
     * GET /todos?title=
     */
    @Test
    public void testGetTodosWithQuery() throws IOException {
        ApiResponse<TodoReadResult> response = httpGet("/todos?title=scan%20paperwork", TodoReadResult.class)
                .assertOkAndFormed();
        TodoReadResult todos = response.getBodyParsed();
        Assert.assertNotNull("Response is null", todos);
        Assert.assertNotEmpty(todos.getTodos());
        Assert.assertEquals("Title mismatched", "scan paperwork", todos.getTodos().get(0).getTitle());
    }

    /**
     * GET /todos/2
     */
    @Test
    public void testGetTodosId2() throws IOException {
        ApiResponse<TodoReadResult> response = httpGet("/todos/2", TodoReadResult.class)
                .assertOkAndFormed();
        TodoReadResult todos = response.getBodyParsed();
        Assert.assertNotNull("Response is null", todos);
        Assert.assertEquals("Response elements count is not 1", 1, todos.getTodos().size());
        Todo todo = todos.getTodos().get(0);
        Assert.assertEquals("Title mismatched", "file paperwork", todo.getTitle());
        Assert.assertNotNull("Response 'tasksof' is null", todo.getTasksof());
        Assert.assertEquals("Response 'tasksof' count is not 1", 1, todo.getTasksof().size());
        Assert.assertEquals("Response 'tasksof'[0] invalid", "1", todo.getTasksof().get(0).getId());
    }

    /**
     * GET /todo
     */
    @Test
    public void testGetTodosWithTypo() throws IOException {
        Assert.assertEquals("/todo", HttpStatus.SC_NOT_FOUND, httpGet("/todo", TodoReadResult.class).getStatusCode());
        ApiResponse<TodoReadResult> todos10 = httpGet("/todo", TodoReadResult.class);
        Assert.assertEquals("/todos/10", HttpStatus.SC_NOT_FOUND, todos10.getStatusCode());
    }

    // ----------------------

    /**
     * HEAD /todos
     */
    @Test
    public void testHeadTodos() throws IOException {
        httpHead("/todos").assertOkAndFormed();
    }

    /**
     * HEAD /todos/1
     */
    @Test
    public void testHeadTodosId1() throws IOException {
        httpHead("/todos/1").assertOkAndFormed();
    }

    // ----------------------

    /**
     * POST /todos
     */
    @Test
    public void testPostTodos() throws IOException {
        final ApiResponse<TodoWriteResult> response = httpPost("/todos", null, TodoWriteResult.class);
        Assertions.assertAll(
                () -> response.assertStatusAndFormed(HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals("title : field is mandatory", response.getBodyParsed().getErrorMessages()[0])
        );
    }

    private void testPostTodosNotEmpty(String contentType, String payload, String title) throws IOException {
        final ApiResponse<TodoWriteResult> response = httpPost("/todos", contentType, payload, TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(title, response.getBodyParsed().getTitle());
        deleteTodo(response.getBodyParsed().getId());
    }

    /**
     * POST /todos?
     * Content type: json
     */
    @Test
    public void testPostTodosJSONNotEmpty() throws IOException {
        testPostTodosNotEmpty(CONTENT_TYPE_JSON, "{\"title\":\"TestTile1\"}", "TestTile1");
    }

    /**
     * POST /todos?
     * Content type: xml
     */
    @Test
    public void testPostTodosXMLNotEmpty() throws IOException {
        testPostTodosNotEmpty(CONTENT_TYPE_XML, "<todo><title>TestTile2</title></todo>", "TestTile2");
    }

    // ----------------------

    private void testPostTodosWithId(String contentType, String payload) throws IOException {
        ApiResponse<TodoWriteResult> response = httpPost("/todos", contentType, payload, TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_BAD_REQUEST);
        Assert.assertEquals("Invalid Creation: Failed Validation: Not allowed to create with id", response.getBodyParsed().getErrorMessages()[0]);
    }

    /**
     * POST /todos?id=156
     * Content type: json
     */
    @Test
    public void testPostTodosJSONWithId() throws IOException {
        testPostTodosWithId(CONTENT_TYPE_JSON, "{\"title\":\"TestTitle\",\"id\":156}");
    }

    /**
     * POST /todos?id=157
     * Content type: xml
     */
    @Test
    public void testPostTodosXMLWithId() throws IOException {
        testPostTodosWithId(CONTENT_TYPE_XML, "<todo><title>TestTitle</title><id>157</id></todo>");
    }

    // ----------------------

    private void testPostTodosWithDoneStatus(String contentType, String payload, Boolean doneStatus) throws IOException {
        ApiResponse<TodoWriteResult> response = httpPost("/todos", contentType, payload, TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(doneStatus, response.getBodyParsed().getDoneStatus());
        deleteTodo(response.getBodyParsed().getId());
    }

    /**
     * POST /todos?doneStatus=true
     * Content type: json
     */
    @Test
    public void testPostTodosJSONWithDoneStatus() throws IOException {
        testPostTodosWithDoneStatus(CONTENT_TYPE_JSON, "{\"title\":\"TodoTemp\",\"doneStatus\":true}", true);
    }

    /**
     * POST /todos?doneStatus=false
     * Content type: xml
     */
    @Test
    public void testPostTodosXMLWithDoneStatus() throws IOException {
        testPostTodosWithDoneStatus(CONTENT_TYPE_XML, "<todo><title>TodoTemp</title><doneStatus>false</doneStatus></todo>", false);
    }

    // ----------------------

    private void testPostTodosWithDescription(String contentType, String payload, String description) throws IOException {
        ApiResponse<TodoWriteResult> response = httpPost("/todos", contentType, payload, TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(description, response.getBodyParsed().getDescription());
        deleteTodo(response.getBodyParsed().getId());
    }

    /**
     * POST /todos?description=description1156
     * Content type: json
     */
    @Test
    public void testPostTodosJSONWithDescription() throws IOException {
        testPostTodosWithDescription(CONTENT_TYPE_JSON, "{\"title\":\"TodoTemp\",\"doneStatus\":true,\"description\":\"description1156\"}", "description1156");
    }

    /**
     * POST /todos?description=description1157
     * Content type: xml
     */
    @Test
    public void testPostTodosXMLWithDescription() throws IOException {
        testPostTodosWithDescription(CONTENT_TYPE_XML, "<todo><title>TodoTemp</title><doneStatus>true</doneStatus><description>description1157</description></todo>", "description1157");
    }

    // ----------------------

    private static final String TODOS1 = "{\"doneStatus\": false,\"description\": \"\",\"title\": \"scan paperwork\",\n \"categories\": [\n{\n\"id\": \"1\"\n}\n],\n\"tasksof\": [\n{\n\"id\": \"1\"\n}\n]\n}";

    private void testPutTodosWithTitle(String contentType, String payload, String title) throws IOException {
        ApiResponse<TodoWriteResult> response = httpPut("/todos/1", contentType, payload, TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_OK);
        Assert.assertEquals(title, response.getBodyParsed().getTitle());
        httpPut("/todos/1", CONTENT_TYPE_JSON, TODOS1, TodoWriteResult.class);
    }

    /**
     * PUT /todos?title=title33
     * Content type: json
     */
    @Test
    public void testPutTodosJSONWithTitle() throws IOException {
        testPutTodosWithTitle(CONTENT_TYPE_JSON, "{\"title\":\"title33\",\"description\":\"description12\"\n}", "title33");
    }

    /**
     * PUT /todos?description=title 34
     * Content type: xml
     */
    @Test
    public void testPutTodosXMLWithTitle() throws IOException {
        testPutTodosWithTitle(CONTENT_TYPE_XML, "<todo><title>title 34</title><description>description13</description></todo>", "title 34");
    }

    // ----------------------

    /**
     * DELETE /todos
     */
    @Test
    public void testDeleteTodos() throws IOException {
        ApiResponse<TodoWriteResult> postResult = httpPost("/todos", "{\"title\":\"title44\",\"description\":\"description75\"}", TodoWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        ApiResponse<TodoReadResult> deleteBefore = httpGet("/todos", TodoReadResult.class).assertOkAndFormed();
        httpDelete("/todos/" + postResult.getBodyParsed().getId(), void.class).assertStatusAndFormed(HttpStatus.SC_OK);
        ApiResponse<TodoReadResult> deleteAfter = httpGet("/todos", TodoReadResult.class).assertOkAndFormed();
        Assert.assertEquals(1, deleteBefore.getBodyParsed().getTodos().size() - deleteAfter.getBodyParsed().getTodos().size());
    }

    // -------------------------------------------------------------

    private void deleteTodo(String id) throws IOException {
        httpDelete("/todos/" + id, Object.class);
    }
}
