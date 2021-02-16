package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestCategoriesIdProjects extends TestInitialization {
    //GET
    @Test
    public void testGetnWithInvaildIDStatus1() {
        assertGetStatusHelper("/categories/100/projects", STATUS_OK);
    }

    @Test
    public void testGetWithInvaildIDStatus2() {
        assertGetStatusHelper("/categories/100/projects", STATUS_BAD_REQUEST);
    }

    @Test
    public void testGetWithVaildIDArrayLenth() {
        HttpResponse<JsonNode> response = Unirest.get("/categories/2/projects").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("projects").length(), 0);
    }

    @Test
    public void testGetWithVaildIDXMLStatusCode() {
        HttpResponse<String> response = Unirest.get("/categories/2/projects").asString();
        assertEquals(response.getStatus(), STATUS_OK);
    }

    //PUT
    public void testPutStatusCode() {
        assertPutStatusHelper("/categories/1/projects", STATUS_NOTSUPPORT);
    }

    //POST
    @Test
    public void testPostWithValidCategoryIdStatus() {
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/projects")
                .header("Content-Type", "application/json")
                .asJson();

        //Reset database status
        deleteProjectsOfCategoryIdHelper(response, 1);

        assertEquals(response.getStatus(), STATUS_CREATED);
    }

    @Test
    public void testPostWithValidIdAndInvalidFieldNameStatus() {
        //malformed JSON
        HttpResponse<JsonNode> response = Unirest.post("/categories/1/projects")
                .header("Content-Type", "application/json")
                .body("{\n   \"title\":\"New Task\",\n   \"completed\":true,\n   \"other\":\"name\"\n}\n")
                .asJson();

        assertEquals(response.getStatus(), STATUS_BAD_REQUEST);
    }

//    @Test
//    public void testPostWithValidCategoryIdXMLStatus() {
//        HttpResponse<String> response = Unirest.post("/categories/1/projects")
//                .header("Content-Type", "application/json")
//                .header("accept", "application/xml")
//                .asString();
//
//        //Reset database status
//        deleteProjectsOfCategoryIdHelper(response, 1);
//        assertEquals(response.getStatus(), STATUS_CREATED);
//    }

    //DELETE
    @Test
    public void testDeleteStatusCode() {
        assertDeleteStatusHelper("/categories/1/projects", STATUS_NOTSUPPORT);
    }

    //PATCH
    @Test
    public void testPatchStatusCode() {
        assertPatchStatusHelper("/categories/1/projects", STATUS_NOTSUPPORT);
    }

    //OPTIONS
    @Test
    public void testOptionsStatusCode() {
        assertOptionsStatusHelper("/categories/1/projects", STATUS_OK);
    }
    @Test
    public void testOptionsStatusCode2() {
        assertOptionsStatusHelper("/categories/3/projects", STATUS_OK);
    }

    //HEAD
    @Test
    public void testHeadStatusCode() {
        assertHeadStatusHelper("/categories/1/projects", STATUS_OK);
    }
    @Test
    public void testHeadStatusCode2() {
        assertHeadStatusHelper("/categories/3/projects", STATUS_OK);
    }

    // Delete the relationship along with object created
    public void deleteProjectsOfCategoryIdHelper(HttpResponse<JsonNode> response, int id) {
        Unirest.delete("/categories/" + String.valueOf(id) + "/projects/" + String.valueOf(response.getBody().getObject().getInt("id"))).asJson();
        Unirest.delete("/projects/" + String.valueOf(response.getBody().getObject().getInt("id"))).asJson();
    }

}
