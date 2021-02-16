package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@RunWith(RandomTestRunner.class)
public class TestCategories extends TestInitialization {

    // GET
    @Test
    public void testGetCategoryStatus() {
        assertGetStatusHelper("/categories", STATUS_OK);
    }

    @Test
    public void testGetFilterWithValidPara() {
        assertEquals(Unirest.get("/categories?title=Home").asJson().getBody().getObject().
                getJSONArray("categories").getJSONObject(0).getString("title"), "Home");
    }

    @Test
    public void testGetFilterWithInvalidPara() {
        HttpResponse<JsonNode> response = Unirest.get("/categories?a=0").asJson();
        String title = response.getBody().getObject().getJSONArray("categories").getJSONObject(0).getString("title");
        assertTrue(title.equals("Home") || title.equals("Office"));
    }

    @Test
    public void testGetCategoryGenerateXML() {
        HttpResponse<String> response = Unirest.get("/categories").header("accept", "application/xml").asString();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    //HEAD
    @Test
    public void testHeadCategoryStatus() {
        assertHeadStatusHelper("/categories", STATUS_OK);
    }

    //POST
    @Test
    public void testPostCategoryStatusWithoutFieldValue() {
        HttpResponse<JsonNode> response = Unirest.post("/categories").asJson();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

    @Test
    public void testPostCategoryStatusWithValidFieldValue() {
        HttpResponse<JsonNode> response = Unirest.post("/categories").body("{\n\"title\":\"Emergency\",\n  \"description\":\"Urgent work\"\n}").asJson();
        // Restore the system to the initial state
        deleteCategoryIdHelper(response);
        assertEquals(response.getStatus(), STATUS_CREATED);
    }

    @Test
    public void testPostCategoryStatusWithValidFieldValue1() {
        HttpResponse<JsonNode> response = Unirest.post("/categories").body("{\n\"title\":\"Emergency\",\n  \"description\":\"Urgent work\"\n}").asJson();
        // Restore the system to the initial state
        deleteCategoryIdHelper(response);
        assertEquals(response.getBody().getObject().getString("title"), "Emergency");
    }

    @Test
    public void testPostCategoryStatusWithUnknownFieldValue() {
        HttpResponse<JsonNode> response = Unirest.post("/categories").body("{\n\"title\":\"Emergency\",\n  \"description\":\"Urgent work\",\n   \"other\":\"note\"}").asJson();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

    @Test
    public void testPostCategoryGenerateXML() {
        HttpResponse<String> response = Unirest.post("/categories").body("{\n\"title\":\"Emergency\",\n  \"description\":\"Urgent work\",\n   \"other\":\"note\"}").header("accept", "application/xml").asString();
        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

    //PUT
    @Test
    public void testPutCategoryStatus() {
        assertPutStatusHelper("/categories", STATUS_NOTSUPPORT);
    }

    //DELETE
    @Test
    public void testDeleteCategoryStatus() {
        assertDeleteStatusHelper("/categories", STATUS_NOTSUPPORT);
    }

    //PATCH
    @Test
    public void testPatchCategoryStatus() {
        assertPatchStatusHelper("/categories", STATUS_NOTSUPPORT);
    }

    //OPTIONS
    @Test
    public void testOptionsCategoryStatus() {
        assertOptionsStatusHelper("/categories", STATUS_OK);
    }

    // Helper Methods
    // Delete the relationship along with object created
    public void deleteCategoryIdHelper(HttpResponse<JsonNode> response) {
        Unirest.delete("/categories/" + String.valueOf(response.getBody().getObject().getInt("id"))).asJson();
    }

}