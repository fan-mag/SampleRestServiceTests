package entity.helpers;

import entity.builders.RequestBuilder;
import io.restassured.http.Method;


public class LoginHelper extends BaseHelper {
    private final String loginServiceURI = properties.getProperty("loginServiceURI");

    public void loginPut(String login, String password, Boolean isContentTypePresent, Boolean isJsonValid) {
        logger.debug(String.format("Login PUT with login %s, password %s, ContentType: %b, JsonValid: %b",
                login, password, isContentTypePresent, isJsonValid));
        request = new RequestBuilder();
        if (isJsonValid && login != null)
            request.withBody("login", login);
        if (isJsonValid && password != null)
            request.withBody("password", password);
        if (!isJsonValid)
            request.withBody("notvalidjson", "failthis");
        if (isContentTypePresent != null)
            if (isContentTypePresent)
                request.withContentType();
            else
                request.withHeader("Content-Type", "text/plain");
        request.withServiceUri(loginServiceURI).method(Method.PUT).execute();
        response = request.response();
    }

    public void login(String login, String password) {
        if (login.equals("<Отсутствует>")) login = null;
        if (password.equals("<Отсутствует>")) password = null;
        loginPut(login, password, true, true);
    }

    public void loginWithContentType(String login, String password, String contentType) {
        Boolean isContentTypePresent = null;
        if (!contentType.equals("<Отсутствует>"))
            isContentTypePresent = contentType.equals("application/json");
        loginPut(login, password, isContentTypePresent, true);
    }

    public void loginWithJsonBody(String login, String password, String jsonBody) {
        Boolean isJsonBodyPresent = null;
        if (!jsonBody.equals("<Отсутствует>"))
            isJsonBodyPresent = jsonBody.equals("Корректный");
        loginPut(login, password, true, isJsonBodyPresent);
    }

    public void loginDelete(String cookie) {
        logger.debug(String.format("Login DELETE with Cookie %s", cookie));
        request = new RequestBuilder();
        if (!cookie.equals("<Отсутствует>"))
            request.withHeader("Cookie", cookie);
        request.withServiceUri(loginServiceURI).method(Method.DELETE).execute();
        response = request.response();
    }

    public void loginGet(String cookie) {
        logger.debug(String.format("Login GET with Cookie %s", cookie));
        request = new RequestBuilder();
        if (!cookie.equals("<Отсутствует>"))
            request.withHeader("Cookie", cookie);
        request.withServiceUri(loginServiceURI).method(Method.GET).execute();
        response = request.response();
    }

    public void loginPostWithCookie(String cookie) {
        loginPost(cookie, true, true, true);
    }

    public void loginPostWithUserId(String cookie, String validUserId) {
        Boolean isUserIdValid = validUserId.equals("Корректный");
        loginPost(cookie, true, true, isUserIdValid);
    }

    public void loginPost(String cookie, String contentType) {
        Boolean isContentTypePresent = null;
        if (!contentType.equals("<Отсутствует>"))
            isContentTypePresent = contentType.equals("application/json");
        loginPost(cookie, isContentTypePresent, true, true);
    }

    public void loginPostWithJson(String cookie, String jsonType) {
        Boolean isJsonCorrect = null;
        if (!jsonType.equals("Пустой"))
            isJsonCorrect = !jsonType.equals("Некорректный");
        loginPost(cookie, true, isJsonCorrect, true);
    }

    public void loginPost(String cookie, Boolean isContentTypePresent, Boolean isJsonCorrect, Boolean isUserValid) {
        logger.debug(String.format("Login POST with Cookie %s, ContentType: %b, JsonCorrect: %b, UserValid: %b",
                cookie, isContentTypePresent, isJsonCorrect, isUserValid));
        request = new RequestBuilder();
        if (cookie != null)
            request.withHeader("Cookie", cookie);
        if (isContentTypePresent != null)
            if (isContentTypePresent)
                request.withContentType();
            else request.withHeader("Content-Type", "text/plain");
        if (isJsonCorrect != null)
            if (isJsonCorrect)
                if (isUserValid)
                    request.withBody("user_id", 3);
                else
                    request.withBody("user_id", "text");
            else
                request.withBody("invalid", "text");
        else
            request.withBody("", "");
        request.withServiceUri(loginServiceURI).method(Method.POST).execute();
        response = request.response();
    }


    public String authCookie(String login, String password) {
        logger.debug(String.format("Getting cookie for login %s, password %s", login, password));
        loginPut(login, password, true, true);
        return response.getCookie();
    }

}
