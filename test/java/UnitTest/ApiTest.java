package UnitTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import UnitTest.model.CategoriesReadResult;
import UnitTest.model.ProjectsReadResult;
import UnitTest.model.TodoReadResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * API test.
 *
 * @author Haohang Xia (created on 2021-02-14)
 * @version 1.0
 */
public abstract class ApiTest implements Closeable {
    static final String CONTENT_TYPE_JSON = "application/json";
    static final String CONTENT_TYPE_XML = "application/xml";

    private static final String API_BASE = "http://localhost:4567";

    private final ObjectMapper jacksonMapper = new ObjectMapper();
    private CloseableHttpClient httpClient;
    private volatile boolean closed;

    ApiTest() {
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setMaxConnTotal(200).setMaxConnPerRoute(20).build();
    }

    // -------------------------------------------------------

    @After
    public void checkSideEffects() throws IOException {
        if (closed) return;
        ApiResponse<TodoReadResult> response = httpGet("/todos", TodoReadResult.class)
                .assertOkAndFormed();
        TodoReadResult todos = response.getBodyParsed();
        Assert.assertNotNull("Response is null", todos);
        Assert.assertEquals("Elements count is not 2", 2, todos.getTodos().size());
        Assert.assertAnyEquals("[0]Title invalid", new String[]{"scan paperwork", "file paperwork"}, todos.getTodos().get(0).getTitle());

        ApiResponse<CategoriesReadResult> response1 = httpGet("/categories", CategoriesReadResult.class)
                .assertOkAndFormed();
        CategoriesReadResult categories = response1.getBodyParsed();
        Assert.assertNotNull("Response is null", categories);
        Assert.assertEquals("[0]Title invalid", "Office", categories.getCategories().get(0).getTitle());

        ApiResponse<ProjectsReadResult> response2 = httpGet("/projects", ProjectsReadResult.class)
                .assertOkAndFormed();
        ProjectsReadResult projects = response2.getBodyParsed();
        Assert.assertNotNull("Response is null", projects);
        Assert.assertEquals("[0]Title invalid", "Office Work", projects.getProjects().get(0).getTitle());
    }

    // -------------------------------------------------------

    /**
     * Execute API request.
     *
     * @param request      current request.
     * @param contentType  request content type(MIME).
     * @param responseType response model type.
     * @param <T>          response model type.
     * @return response model instance if successfully.
     * @throws IOException I/O exception.
     */
    <T> ApiResponse<T> requestApi(HttpRequestBase request, String contentType, Class<T> responseType) throws IOException {
        Objects.requireNonNull(request);
        ApiResponse<T> response = new ApiResponse<>();
        response.setBodyMalformed(false);

        // Set HTTP headers.
        request.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
        HttpResponse httpResponse = httpClient.execute(request);
        response.setHttpResponse(httpResponse);

        // Read response body to UTF-8 JSON text.
        HttpEntity responseBody = httpResponse.getEntity();
        if (responseBody != null) {
            String responseText = EntityUtils.toString(responseBody, StandardCharsets.UTF_8);
            response.setBodyText(responseText);
            parseResponseText(responseText, responseType, response);
        }

        return response;
    }

    /**
     * Parse body text to model.
     */
    private <T> void parseResponseText(String responseText, Class<T> responseType, ApiResponse<T> out) {
        if (responseType == void.class) return;
        try {
            out.setBodyParsed(jacksonMapper.readValue(responseText, responseType));
            out.setBodyMalformed(false);
        } catch (Exception e) {
            out.setBodyParseError(e);
            out.setBodyMalformed(true);
        }
    }

    <T> ApiResponse<T> httpGet(String path, String contentType, Class<T> responseType) throws IOException {
        return requestApi(new HttpGet(API_BASE + path), contentType, responseType);
    }

    <T> ApiResponse<T> httpGet(String path, Class<T> responseType) throws IOException {
        return httpGet(path, CONTENT_TYPE_JSON, responseType);
    }

    ApiResponse httpHead(String path) throws IOException {
        return requestApi(new HttpHead(API_BASE + path), CONTENT_TYPE_JSON, void.class);
    }

    <T> ApiResponse<T> httpPost(String path, String contentType, String payload, Class<T> responseType) throws IOException {
        HttpPost httpPost = new HttpPost(API_BASE + path);
        if (payload != null) httpPost.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        return requestApi(httpPost, contentType, responseType);
    }

    <T> ApiResponse<T> httpPost(String path, String payload, Class<T> responseType) throws IOException {
        return httpPost(path, CONTENT_TYPE_JSON, payload, responseType);
    }

    <T> ApiResponse<T> httpPut(String path, String contentType, String payload, Class<T> responseType) throws IOException {
        HttpPut httpPut = new HttpPut(API_BASE + path);
        if (payload != null) httpPut.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        return requestApi(httpPut, contentType, responseType);
    }

    <T> ApiResponse<T> httpDelete(String path, Class<T> responseType) throws IOException {
        return requestApi(new HttpDelete(API_BASE + path), CONTENT_TYPE_JSON, responseType);
    }

    // -------------------------------------------------------

    @Override
    public synchronized void close() throws IOException {
        if (closed) return;
        closed = true;
        httpClient.close();
        httpClient = null;
    }
}
