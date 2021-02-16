package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestCategoriesIdProjectsId extends TestCategories{
    //GET
    @Test
    public void testGetStatusCode() {
        assertGetStatusHelper("/categories/1/projects/1", STATUS_NOTSUPPORT);
    }

    //POST
    @Test
    public void testPostStatusCode() {
        assertPostStatusHelper("/categories/1/projects/1", STATUS_NOTSUPPORT);
    }

    //OPTIONS
    @Test
    public void testOptionsStatusCode() {
        assertOptionsStatusHelper("/categories/1/projects/1", STATUS_OK);
    }

    //PUT
    @Test
    public void testPutStatusCode() {
        assertPutStatusHelper("/categories/1/projects/1", STATUS_NOTSUPPORT);
    }

    //HEAD
    @Test
    public void testHeadStatusCode() {
        assertHeadStatusHelper("/categories/1/projects/1", STATUS_NOTSUPPORT);
    }

    //PATCH
    @Test
    public void testPatchStatusCode() {
        assertPatchStatusHelper("/categories/1/projects/1", STATUS_NOTSUPPORT);
    }

    //DELETE
    @Test
    public void testDeleteWithValidIdStatusCode() {
        // create relation to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/projects").header("Content-Type", "application/xml").asJson();
        int id = response.getBody().getObject().getInt("id");

        response = Unirest.delete("/categories/1/projects/" + String.valueOf(id))
                .header("Content-Type", "application/json").asJson();

        // Must also delete projects
        Unirest.delete("/projects/" + String.valueOf(id)).asJson();

        assertEquals(response.getStatus(), STATUS_OK);
    }

    @Test
    public void testDeleteWithInvalidIDStatusCode() {
        // create relation to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/projects").header("Content-Type", "application/xml").asJson();
        int id = response.getBody().getObject().getInt("id");

        //invalid project id
        response = Unirest.delete("/categories/1/projects/100")
                .header("Content-Type", "application/json").asJson();

        // Must also delete projects
        Unirest.delete("/projects/" + String.valueOf(id)).asJson();
        Unirest.delete("/categories/1/projects/" + String.valueOf(id))
                .header("Content-Type", "application/json").asJson();

        assertEquals(response.getStatus(), STATUS_NOTFOUND);
    }

    @Test
    public void testDeleteWithValidIdArrayLength() {
        // create project category to delete
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/projects").header("Content-Type", "application/xml").asJson();

        int id = response.getBody().getObject().getInt("id");

        response = Unirest.get("/categories/1/projects").asJson();
        int len = response.getBody().getObject().getJSONArray("projects").length()-1;

        response = Unirest.delete("/categories/1/projects/" + String.valueOf(id))
                .header("Content-Type", "application/json").asJson();

        // Must also delete projects
        Unirest.delete("/projects/" + String.valueOf(id)).asJson();

        response = Unirest.get("/categories/1/projects").asJson();

        assertEquals(response.getBody().getObject().getJSONArray("projects").length(), len);
    }
}
