package UnitTest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

/**
 * API response wrap bean.
 *
 * @author Haohang Xia (created on 2021-02-15)
 * @version 1.0
 */
public class ApiResponse<T> {
    private HttpResponse httpResponse;
    private int statusCode;
    private String bodyText;
    private boolean bodyMalformed;
    private T bodyParsed;
    private Throwable bodyParseError;

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public boolean isBodyMalformed() {
        return bodyMalformed;
    }

    public void setBodyMalformed(boolean bodyMalformed) {
        this.bodyMalformed = bodyMalformed;
    }

    public T getBodyParsed() {
        return bodyParsed;
    }

    public void setBodyParsed(T bodyParsed) {
        this.bodyParsed = bodyParsed;
    }


    public Throwable getBodyParseError() {
        return bodyParseError;
    }

    public void setBodyParseError(Throwable bodyParseError) {
        this.bodyParseError = bodyParseError;
    }

    public ApiResponse<T> assertOkAndFormed() {
        return assertStatusAndFormed(HttpStatus.SC_OK);
    }

    public ApiResponse<T> assertStatusAndFormed(int statusCode) {
        Assert.assertEquals(statusCode, getStatusCode());
        Assert.assertFalse(isBodyMalformed());
        return this;
    }
}
