package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
@RunWith(RandomTestRunner.class)
public class TestProjectsIDTask extends TestInitialization{


    //GET
    @Test
    public void TestGETpjIDtaskStaus(){
        assertEquals(Unirest.get("/projects/1/tasks").asJson().getStatus(),200);
    }
    @Test
    public void TestGETpjIDtaskLength(){
        assertEquals(Unirest.get("/projects/1/tasks").asJson().getBody().getObject().getJSONArray("todos").length(),2);
    }
    //HEAD
    @Test
    public void TestHEADpjIDtaskStatus()
    {
        assertEquals(Unirest.head("/projects/1/tasks").asJson().getStatus(),200);
    }
    //POST
    @Test
    public void TestPOSTpjIDtaskStatus(){
        //create a new task
        HttpResponse<JsonNode> response=   Unirest.post("/projects/1/tasks").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        int taskID=response.getBody().getObject().getInt("id");
        //attach new task to the project 1
        assertEquals(response.getStatus(),201);


        Unirest.delete("/projects/1/tasks/"+taskID).asJson();
        //delete new task
        Unirest.delete("/todos/"+taskID).asJson();

    }
    @Test
    public void TestPOSTpjIDtaskLength(){

        HttpResponse<JsonNode> response=   Unirest.post("/projects/1/tasks").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();

        assertEquals(Unirest.get("/projects/1/tasks").asJson().getBody().getObject().getJSONArray("todos").length(),3);

        int taskID=response.getBody().getObject().getInt("id");
        Unirest.delete("/projects/1/tasks/"+taskID).asJson();
        //delete new task
        Unirest.delete("/todos/"+taskID).asJson();
    }
    @Test
    public void TestPOSTpjIDtaskTittle(){
        HttpResponse<JsonNode> response=Unirest.post("/projects/1/tasks").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        assertEquals(response.getBody().getObject().getString("title"),"New Test");

        int taskID=response.getBody().getObject().getInt("id");
        Unirest.delete("/projects/1/tasks/"+taskID).asJson();
        //delete new task
        Unirest.delete("/todos/"+taskID).asJson();
    }

}
