package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestCategoriesIdTodos extends TestCategories {
    //GET
    @Test
    public void testGetStatusCode() {
        assertGetStatusHelper("/categories/1/todos", STATUS_OK);
    }

    @Test
    public void testGetStatusCode1() {
        assertGetStatusHelper("/categories/3/todos", STATUS_OK);
    }

    @Test
    public void testGetArrayLength() {
        HttpResponse<JsonNode> response = Unirest.get("/categories/1/todos").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("todos").length(), 0);
    }

    //PUT
    @Test
    public void testPutStatusCode() {
        assertPutStatusHelper("/categories/1/todos", STATUS_NOTSUPPORT);
    }

    //HEAD
    @Test
    public void testHeadStatusCode1() {
        assertHeadStatusHelper("/categories/1/todos", STATUS_OK);
    }

    @Test
    public void testHeadStatusCode2() {
        assertHeadStatusHelper("/categories/7/todos", STATUS_OK);
    }

    //PATCH
    @Test
    public void testPatchStatusCode() {
        assertPatchStatusHelper("/categories/1/todos", STATUS_NOTSUPPORT);
    }

    //DELETE
    @Test
    public void testDeleteStatusCode() {
        assertDeleteStatusHelper("/categories/1/todos", STATUS_NOTSUPPORT);
    }

    //OPTIONS
    @Test
    public void testOptionsStatusCode1() {
        assertOptionsStatusHelper("/categories/1/todos", STATUS_OK);
    }

    @Test
    public void testOptionsStatusCode2() {
        assertOptionsStatusHelper("/categories/7/todos", STATUS_OK);
    }

    //POST
    @Test
    public void testPostStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"Test Title\"}").asJson();

        // reset database private state
        deleteTodosOfCategoryIdHelper(response, 1);

        assertEquals(response.getStatus(), STATUS_CREATED);
    }

    @Test
    public void testPostTitle() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"Test Title\"}").asJson();

        // reset database private state
        deleteTodosOfCategoryIdHelper(response, 1);

        assertEquals(response.getBody().getObject().getString("title"), "Test Title");
    }

    @Test
    public void testPostTodoReference() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/2/todos").header("Content-Type", "application/json")
                .body("{\n    \"id\":\"1\"}").asJson();

        HttpResponse<JsonNode> todo_response = Unirest.get("/todos").asJson();
        int len = todo_response.getBody().getObject().getJSONArray("todos").getJSONObject(0).getJSONArray("categories").length();
        // reset database private state
        Unirest.delete("/categories/2/todos/1").asJson();
        assertEquals(len, 2);
    }

    @Test
    public void testPostWithoutField() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").asJson();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

//    @Test
//    public void testPostXMLStatusCode() {
//        HttpResponse<String> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/json")
//                .header("accept", "application/xml")
//                .body("{\n    \"title\":\"Test Title\"}").asString();
//
//        // reset database private state
//        deleteTodosOfCategoryIdHelper(response, 1);
//
//        assertEquals(response.getStatus(), STATUS_CREATED);
//    }

    @Test
    public void testPostCategoryTodosJSONMalformedErrorMessage() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/todos").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"test}").asJson();

        assertEquals(response.getBody().getObject().getJSONArray("errorMessages").getString(0),
                "com.google.gson.stream.MalformedJsonException: Unterminated string at line 2 column 19 path $.");
    }

    // Delete the relationship along with object created
    public void deleteTodosOfCategoryIdHelper(HttpResponse<JsonNode> response, int id) {
        Unirest.delete("/categories/" + String.valueOf(id) + "/todos/" + String.valueOf(response.getBody().getObject().getInt("id"))).asJson();
        Unirest.delete("/todos/" + String.valueOf(response.getBody().getObject().getInt("id"))).asJson();
    }
}
