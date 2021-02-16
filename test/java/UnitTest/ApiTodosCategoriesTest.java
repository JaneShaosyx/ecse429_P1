package UnitTest;

import UnitTest.model.CategoriesReadResult;
import UnitTest.model.CategoriesWriteResult;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Test for API: /todos/:id/categories
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
@RunWith(RandomTestRunner.class)
public class ApiTodosCategoriesTest extends ApiTest {

    /**
     * GET /todos/500/categories
     */
    @Test
    public void testGetTodosCategoriesId500() throws IOException {
        httpGet("/todos/500/categories", CategoriesReadResult.class).assertStatusAndFormed(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * GET /todos/1/categories
     */
    @Test
    public void testGetTodosCategoriesId1() throws IOException {
        ApiResponse<CategoriesReadResult> response = httpGet("/todos/1/categories", CategoriesReadResult.class)
                .assertOkAndFormed();
        Assert.assertEquals(1, response.getBodyParsed().getCategories().size());
        Assert.assertEquals("Office", response.getBodyParsed().getCategories().get(0).getTitle());
    }

    // ----------------------

    /**
     * HEAD /todos/1/categories
     */
    @Test
    public void testHeadTodosCategoriesId1() throws IOException {
        httpHead("/todos/1/categories").assertOkAndFormed();
    }

    // ----------------------

    private void testPostTodosCategoriesWithId(String contentType, String payload) throws IOException {
        ApiResponse<CategoriesWriteResult> response = httpPost("/todos/1/categories", contentType, payload, CategoriesWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_NOT_FOUND);
        Assert.assertEquals("Could not find thing matching value for id", response.getBodyParsed().getErrorMessages()[0]);
    }

    /**
     * POST /todos/1/categories?id=1
     * Content type: json
     */
    @Test
    public void testHeadTodosCategoriesId1JSON() throws IOException {
        testPostTodosCategoriesWithId(CONTENT_TYPE_JSON, "{\"id\":1}");
    }

    /**
     * POST /todos/1/categories?id=1
     * Content type: xml
     */
    @Test
    public void testHeadTodosCategoriesId1XML() throws IOException {
        testPostTodosCategoriesWithId(CONTENT_TYPE_XML, "<category><id>1</id></category>");
    }

    // ----------------------

    private void testPostTodosCategoriesWithTitle(String contentType, String payload, String title) throws IOException {
        ApiResponse<CategoriesWriteResult> response = httpPost("/todos/1/categories", contentType, payload, CategoriesWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(title, response.getBodyParsed().getTitle());
        deleteTodosCategories("1", response.getBodyParsed().getId());
    }

    /**
     * POST /todos/1/categories?title=Title 357
     * Content type: json
     */
    @Test
    public void testPostTodosCategoriesWithTitleJSON() throws IOException {
        testPostTodosCategoriesWithTitle(CONTENT_TYPE_JSON, "{\"title\":\"Title 357\"}", "Title 357");
    }

    /**
     * POST /todos/1/categories?title=Title 356
     * Content type: xml
     */
    @Test
    public void testPostTodosCategoriesWithTitleXML() throws IOException {
        testPostTodosCategoriesWithTitle(CONTENT_TYPE_XML, "<category><title>Title 356</title></category>", "Title 356");
    }

    // ----------------------

    private void testPostTodosCategoriesWithDescription(String contentType, String payload, String description) throws IOException {
        ApiResponse<CategoriesWriteResult> response = httpPost("/todos/1/categories", contentType, payload, CategoriesWriteResult.class)
                .assertStatusAndFormed(HttpStatus.SC_CREATED);
        Assert.assertEquals(description, response.getBodyParsed().getDescription());
        deleteTodosCategories("1", response.getBodyParsed().getId());
    }

    /**
     * POST /todos/1/categories?description=description 357
     * Content type: xml
     */
    @Test
    public void testPostTodosCategoriesWithDescriptionJSON() throws IOException {
        testPostTodosCategoriesWithDescription(CONTENT_TYPE_JSON, "{\"title\":\"Title 357\", \"description\":\"description 357\"}", "description 357");
    }

    /**
     * POST /todos/1/categories?description=description 356
     * Content type: xml
     */
    @Test
    public void testPostTodosCategoriesWithDescriptionXML() throws IOException {
        testPostTodosCategoriesWithDescription(CONTENT_TYPE_XML, "<category><title>Title 356</title><description>description 356</description></category>", "description 356");
    }

    // --------------------------------------

    private void deleteTodosCategories(String todoId, String id) throws IOException {
        httpDelete("/todos/" + todoId + "/categories/" + id, void.class);
        httpDelete("/categories/" + id, void.class);
    }
}
