package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(RandomTestRunner.class)
public class TestProjectsIDTaskID extends TestInitialization{

    @Test
    public void TestDeleteStatus(){

        HttpResponse<JsonNode> response = Unirest.post("/projects/1/tasks").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
        int newTaskID=response.getBody().getObject().getInt("id");

        assertEquals(  Unirest.delete("/projects/1/tasks/"+newTaskID).asJson().getStatus(),200);
        Unirest.delete("/todos/"+newTaskID).asJson();
    }
    @Test
    public void TestDeleteEffect(){

      HttpResponse<JsonNode> response = Unirest.post("/projects/1/tasks").body("{\"title\":\"New Test\", \"description\":\"new description\"}").asJson();
      int prevL=  Unirest.get("/projects/1/tasks").asJson().getBody().getObject().getJSONArray("todos").length();
      int newTaskID=response.getBody().getObject().getInt("id");
      Unirest.delete("/projects/1/tasks/"+newTaskID).asJson();
      int newL=  Unirest.get("/projects/1/tasks").asJson().getBody().getObject().getJSONArray("todos").length();
      Unirest.delete("/todos/"+newTaskID).asJson();
      assertEquals(prevL-newL,1);
    }

}
