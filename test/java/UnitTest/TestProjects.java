package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestProjects extends TestInitialization {
    //GET
    @Test
    public void TestGETprojectsStatus(){
        assertEquals(Unirest.get("/projects").asJson().getStatus(),200);
    }
    @Test
    public void TestGETprojectsSize(){
        assertEquals(Unirest.get("/projects").asJson().getBody().getObject().getJSONArray("projects").length(),1);
    }
    @Test
    public void TestGETprojectActive(){
        assertEquals(Unirest.get("/projects").asJson().getBody().getObject().getJSONArray("projects").getJSONObject(0).getString("active"),"false");
    }
    //HEAD
    @Test
    public void TestHEADprojectsStatus(){
        assertEquals(Unirest.head("/projects").asJson().getStatus(),200);
    }

    //POST
    @Test
    public void TestPOSTprojectsStatus(){

      HttpResponse<JsonNode> response= Unirest.post("/projects").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
      assertEquals(response.getStatus(),201);
      Unirest.delete("/projects/"+response.getBody().getObject().getInt("id")).asEmpty();
    }
    @Test
    public void TestPOSTprojectsTitle(){
        HttpResponse<JsonNode> response= Unirest.post("/projects").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getBody().getObject().getString("title"),"New Test");
        Unirest.delete("/projects/"+response.getBody().getObject().getInt("id")).asEmpty();
    }
    @Test
    public void TestPOSTprojectsErrorMsg(){
        HttpResponse<JsonNode> response= Unirest.post("/projects").body("{\"title\":\"New Test\",\"active\":\"false\", \"description\":\"new description\"}").asJson();
      assertEquals(  response.getBody().getObject().getJSONArray("errorMessages").getString(0),"Failed Validation: active should be BOOLEAN");
    }

}
