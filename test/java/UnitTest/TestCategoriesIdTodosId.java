package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestCategoriesIdTodosId extends TestCategories {
    //GET
    @Test
    public void testGetStatusCode() {
        assertGetStatusHelper("/categories/1/todos/1", STATUS_NOTSUPPORT);
    }

    //PUT
    @Test
    public void testPutStatusCode() {
        assertPutStatusHelper("/categories/1/todos/1", STATUS_NOTSUPPORT);
    }

    //POST
    @Test
    public void testPostStatusCode() {
        assertPostStatusHelper("/categories/1/todos/1", STATUS_NOTSUPPORT);
    }

    //OPTIONS
    @Test
    public void testOptionsStatusCode1() {
        assertOptionsStatusHelper("/categories/1/todos/1", STATUS_OK);
    }

    @Test
    public void testOptionsStatusCode2() {
        assertOptionsStatusHelper("/categories/7/todos/1", STATUS_OK);
    }

    //HEAD
    @Test
    public void testHeadStatusCode() {
        assertHeadStatusHelper("/categories/1/todos/1", STATUS_NOTSUPPORT);
    }

    //PATCH
    @Test
    public void testPatchStatusCode() {
        assertPatchStatusHelper("/categories/1/todos/1", STATUS_NOTSUPPORT);
    }

    //DELETE
    @Test
    public void testDeleteStatusCode() {
        // create project category to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/xml")
                .body("<category><title>Test Title</title></category>").asJson();
        int id = response.getBody().getObject().getInt("id");

        response = Unirest.delete("/categories/1/todos/" + String.valueOf(id))
                .header("Content-Type", "application/json").asJson();

        // Must also delete todos
        Unirest.delete("/todos/" + String.valueOf(id)).asJson();

        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testDeleteInvalidIdStatusCode() {
        HttpResponse<JsonNode> response = Unirest.delete("/categories/1/todos/999")
                .header("Content-Type", "application/json").asJson();

        assertEquals(response.getStatus(), STATUS_NOTFOUND);
    }

    @Test
    public void testDeleteCategoryTodosVerifyDeletion() {
        // create project category to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/xml")
                .body("<category><title>Test Title</title></category>").asJson();

        int id = response.getBody().getObject().getInt("id");

        response = Unirest.get("/categories/1/todos").asJson();
        int len = response.getBody().getObject().getJSONArray("todos").length() - 1;

        response = Unirest.delete("/categories/1/todos/" + String.valueOf(id))
                .header("Content-Type", "application/json").asJson();

        // Must also delete todos
        Unirest.delete("/todos/" + String.valueOf(id)).asJson();

        response = Unirest.get("/categories/1/todos").asJson();

        assertEquals(response.getBody().getObject().getJSONArray("todos").length(), len);
    }


}
