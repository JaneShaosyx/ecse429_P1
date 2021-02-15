package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCategoriesId extends TestInitialization {
    // GET
    @Test
    public void testGetWithValidIdStatusCode() {
        assertGetStatusHelper("/categories/1", STATUS_OK);
    }

    @Test
    public void testGetWithValidIdTitle() {
        HttpResponse<JsonNode> response = Unirest.get("/categories/1").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("categories").getJSONObject(0).getString("title"),
                "Office");
    }

    @Test
    public void testGetWithInvalidIdStatusCode() {
        assertGetStatusHelper("/categories/3", STATUS_NOTFOUND);
    }

    @Test
    public void testGetWithInvalidIdErrorMsg() {
        assertGetErrorMsgHelper("/categories/3", "Could not find an instance with /categories/3", 0);
    }

    @Test
    public void testGetWithValidIdGenerateXML() {
        HttpResponse<String> response = Unirest.get("/categories/1").header("accept", "application/xml").asString();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    //HEAD
    @Test
    public void testHeadWithValidStatus() {
        assertHeadStatusHelper("/categories/1", STATUS_OK);
    }

    @Test
    public void testHeadWithValidStatus3() {
        assertHeadStatusHelper("/categories/3", STATUS_NOTFOUND);
    }

    //POST
    @Test
    public void testPostWithValidIDWithoutFieldStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").asJson();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testPostWithValidIDWithoutFieldTitle() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").asJson();
        assertEquals(response.getBody().getObject().getString("title"), "Office");
    }

    @Test
    public void testPostWithValidIDWitInvalidFieldValueStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"name\":\"true\"\n}").asJson();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

    @Test
    public void testPostWithValidIDWitValidFieldValueStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asJson();
        // Reset to previous state
        Unirest.post("/categories/1").header("Content-Type", "application/json").body("{\n    \"title\": \"Office\",\n  \"description\": \"\"   \n}\n")
                .asJson();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testPostWithInvalidIDWitValidFieldValueStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/3").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asJson();
        assertEquals(response.getStatus(), STATUS_NOTFOUND);
    }

    @Test
    public void testPostWithInvalidIdErrorMsg() {
        assertGetErrorMsgHelper("/categories/3", "No such category entity instance with GUID or ID 3 found", 0);
    }

    @Test
    public void testPostWithValidIdGenerateXML() {
        HttpResponse<String> response = Unirest.get("/categories/1").header("accept", "application/xml").asString();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    //PUT
    @Test
    public void testPutWithValidIDWithoutFieldStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").asJson();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

    @Test
    public void testPutWithInvalidIDWithFieldStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/3").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asJson();
        assertEquals(response.getStatus(), STATUS_NOTFOUND);
    }

    @Test
    public void testPutWithInvalidIDWithoutFieldStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/3").asJson();
        assertEquals(response.getStatus(), STATUS_NOTFOUND);
    }

    @Test
    public void testPutWithValidIDWithValidFieldStatusCode() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asJson();
        // Reset to previous state
        Unirest.post("/categories/1").header("Content-Type", "application/json").body("{\n    \"title\": \"Office\",\n  \"description\": \"\"   \n}\n")
                .asJson();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testPutWithValidIDWithValidFieldXML() {
        HttpResponse<String> response = Unirest.post("/categories/1").header("accept", "application/xml")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asString();
        // Reset to previous state
        Unirest.post("/categories/1").header("Content-Type", "application/json").body("{\n    \"title\": \"Office\",\n  \"description\": \"\"   \n}\n")
                .asJson();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testPutWithValidIDWithValidFieldTitle() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"New Description Title\"\n}").asJson();
        // Reset to previous state
        Unirest.post("/categories/1").header("Content-Type", "application/json").body("{\n    \"title\": \"Office\",\n  \"description\": \"\"   \n}\n")
                .asJson();
        assertEquals(response.getBody().getObject().getString("title"), "New Title");
    }

    //DELETE
    @Test
    public void testDeleteWithValidIDStatusCode() {
        // create project to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"Test description\"\n}").asJson();

        int id = response.getBody().getObject().getInt("id");
        response = Unirest.delete("/categories/" + String.valueOf(id)).header("Content-Type", "application/json")
                .asJson();
        assertEquals(response.getStatus(), STATUS_OK);

    }

    @Test
    public void testDeleteWithValidIDArrayLength() {
        // create project to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories").header("Content-Type", "application/json")
                .body("{\n    \"title\":\"New Title\",\n    \"description\":\"Test description\"\n}").asJson();

        int id = response.getBody().getObject().getInt("id");
        HttpResponse<JsonNode> new_response = Unirest.get("/categories").header("Content-Type", "application/json").asJson();
        int len = new_response.getBody().getObject().getJSONArray("categories").length() - 1;
        Unirest.delete("/categories/" + String.valueOf(id)).header("Content-Type", "application/json")
                .asJson();
        response = Unirest.get("/categories").header("Content-Type", "application/json").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("categories").length(), len);

    }

    @Test
    public void testDeleteWithInvalidIDStatusCode() {
        assertDeleteStatusHelper("/categories/3", STATUS_NOTFOUND);
    }

    @Test
    public void testDeleteWithInvalidIDErrorMsg() {
        HttpResponse<JsonNode> response = Unirest.delete("/categories/3").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("errorMessages").getString(0), "Could not find any instances with categories/3");
    }

    //OPTIONS
    @Test
    public void testOptionsWithValidIDStatusCode() {
        assertOptionsStatusHelper("/categories/1", STATUS_OK);
    }

    @Test
    public void testOptionsWithInvalidIDStatusCode() {
        assertOptionsStatusHelper("/categories/3", STATUS_OK);
    }

    //PATCH
    @Test
    public void testPatchStatusCode() {
        assertPatchStatusHelper("/categories/1", STATUS_NOTSUPPORT);
    }

}
