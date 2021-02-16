package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestProjectsID extends TestInitialization{

    //GET
    @Test
    public void TestGETpjIDStatus(){
        assertEquals(Unirest.get("/projects/1").asJson().getStatus(),200);
    }
    @Test
    public void TestGETpjIDTitle(){
        assertEquals(Unirest.get("/projects/1").asJson().getBody().getObject().getJSONArray("projects").getJSONObject(0).getString("title"),"Office Work");
    }

    //HEAD
    @Test
    public void TestHEADpjIDStatus(){
        assertEquals(Unirest.head("/projects/1").asJson().getStatus(),200);
    }

    //POST
    @Test
    public void TestPOSTpjIDwithValidInputStatus(){
        HttpResponse<JsonNode> response= Unirest.post("/projects/1").body("{\"title\":\"New Test2\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getStatus(),200);
        Unirest.post("/projects/1").body("{\"title\":\"Office Work\", \"completed\":false, \"description\":\"\"}").asJson();
    }
    @Test
    public void TestPOSTpjIDwithInvalidInputStatus(){
        HttpResponse<JsonNode> response= Unirest.post("/projects/1").body("{\"yourName\":\"xxxss\",\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getStatus(),400);
    }
    @Test
    public void TestPOSTpjIDwithInvalidInputMsg(){
        HttpResponse<JsonNode> response= Unirest.post("/projects/1").body("{\"yourName\":\"xxxss\",\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getBody().getObject().getJSONArray("errorMessages").getString(0),"Could not find field: yourName");
    }
    @Test
    public void TestPOSTpjIDTitle(){
        HttpResponse<JsonNode> response= Unirest.post("/projects/1").body("{\"title\":\"New Title\"}").asJson();
        assertEquals(response.getBody().getObject().getString("title"), "New Title");
        Unirest.post("/projects/1").body("{\"title\":\"Office Work\"}").asJson();
    }
    @Test
    public void TestPOSTpjXMLstatus(){
        HttpResponse<JsonNode> response = Unirest.post("/projects/1").header("Content-Type", "application/xml").body("<project><title>New Title</title><completed>true</completed></project>\n").asJson();
        assertEquals(response.getStatus(), 200);
        Unirest.post("/projects/1").body("{\"title\":\"Office Work\", \"completed\":false, \"description\":\"\"}").asJson();
    }
}
