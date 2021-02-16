package UnitTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RandomTestRunner.class)
public class TestInitialization {
    private static final String URL = "http://localhost:4567";

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOTFOUND = 404;
    public static final int STATUS_NOTSUPPORT = 405;
    public static boolean isShutdown = false;

    @BeforeClass
    public static void environmentSetUp() {
        Unirest.config().defaultBaseUrl(URL);
        startServerAPI();
    }

    @After
    public void checkSideEffect() {
        if (isShutdown) {
            isShutdown = !isShutdown;
            return;
        }
        String title = Unirest.get("/todos").asJson().getBody().getObject().getJSONArray("todos").getJSONObject(0).getString("title");
        assertTrue(title.equals("scan paperwork") || title.equals("file paperwork"));
        assertEquals("Office Work", Unirest.get("/projects").asJson().getBody().getObject().getJSONArray("projects").getJSONObject(0).getString("title"));
        HttpResponse<JsonNode> response = Unirest.get("/categories").asJson();
        String title1 = response.getBody().getObject().getJSONArray("categories").getJSONObject(0).getString("title");
        assertTrue(title1.equals("Home") || title1.equals("Office"));
    }

    public static void assertGetStatusHelper(String url, int status_code) {
        assertEquals(Unirest.get(url).asJson().getStatus(), status_code);
    }

    public static void assertHeadStatusHelper(String url, int status_code) {
        assertEquals(Unirest.head(url).asJson().getStatus(), status_code);
    }

    public static void assertPutStatusHelper(String url, int status_code) {
        assertEquals(Unirest.put(url).asJson().getStatus(), status_code);
    }

    public static void assertDeleteStatusHelper(String url, int status_code) {
        assertEquals(Unirest.delete(url).asJson().getStatus(), status_code);
    }

    public static void assertPatchStatusHelper(String url, int status_code) {
        assertEquals(Unirest.patch(url).asJson().getStatus(), status_code);
    }

    public static void assertOptionsStatusHelper(String url, int status_code) {
        assertEquals(Unirest.options(url).asJson().getStatus(), status_code);
    }

    public static void assertPostStatusHelper(String url, int status_code) {
        assertEquals(Unirest.post(url).asJson().getStatus(), status_code);
    }

    public static void assertGetErrorMsgHelper(String url, String expected_message, int index) {
        assertEquals(Unirest.get(url).asJson().getBody().getObject().getJSONArray("errorMessages").getString(index), expected_message);
    }

    public static void startServerAPI() {
        try {
            final InputStream inputS = new ProcessBuilder("java", "-jar", "../../Application_Being_Tested/runTodoManagerRestAPI-1.5.5.jar").start().getInputStream();
            final InputStreamReader inputSR = new InputStreamReader(inputS);
            final BufferedReader output = new BufferedReader(inputSR);
            while (true) {
                String in = output.readLine();
                if (in.contains("Running on 4567"))
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
