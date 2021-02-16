package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestProjectIDCategories extends TestInitialization {
    //GET
    @Test
    public void TestGETpjIDctgStaus(){

        assertEquals(Unirest.get("/projects/1/categories").asJson().getStatus(),200);
    }
    @Test
    public void TestGETpjIDctgLength(){
        assertEquals(Unirest.get("/projects/1/categories").asJson().getBody().getObject().getJSONArray("categories").length(),0);
    }
    //HEAD
    @Test
    public void TestHEADpjIDctgStatus()
    {
        assertEquals(Unirest.head("/projects/1/categories").asJson().getStatus(),200);
    }
    //POST
    @Test
    public void TestPOSTpjIDctgStatus(){
        //create a new task
        HttpResponse<JsonNode> response=Unirest.post("/projects/1/categories").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        int ctgID=response.getBody().getObject().getInt("id");
        //attach new task to the project 1
        assertEquals(response.getStatus(),201);


        Unirest.delete("/projects/1/tasks/"+ctgID).asJson();
        //delete new task
        Unirest.delete("/categories/"+ctgID).asJson();

    }

    @Test
    public void TestPOSTpjIDctgTittle(){
        HttpResponse<JsonNode> response=Unirest.post("/projects/1/categories").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getBody().getObject().getString("title"),"New Test");

        int ctgID=response.getBody().getObject().getInt("id");
        Unirest.delete("/projects/1/tasks/"+ctgID).asJson();
        //delete new task
        Unirest.delete("/categories/"+ctgID).asJson();
    }
}
