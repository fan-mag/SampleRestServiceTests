package entity.helpers;

import entity.builders.RequestBuilder;
import io.restassured.http.Method;


public class LoginHelper extends BaseHelper {
    private final String loginServiceURI = properties.getProperty("loginServiceURI");

    public void loginPut(String login, Object password, String contentType, String bodyType) {
        request = new RequestBuilder();
        switch (contentType) {
            case "Корректный":
                request.withHeader("Content-Type", "application/json");
                break;
            case "Некорректный":
                request.withHeader("Content-Type", "application/xml");
                break;
            case "Пустой":
                request.withHeader("Content-Type", "text/plain");
                break;
            case "Отсутствует":
                break;
        }
        switch (bodyType) {
            case "Корректный":
            case "Значение int вместо String":
                request.withBody("login", login);
                request.withBody("password", password);
                break;
            case "Без login":
                request.withBody("password", password);
                break;
            case "Без password":
                request.withBody("login", login);
                break;
            case "Пустой json":
                request.withBody("{}");
                break;
            case "Пустой json массив":
                request.withBody("[]");
                break;
            case "Формат не json":
                request.withBody(String.format("{" +
                        "\"login\":\"%s\"" +
                        "\"password\":\"%s\"" +
                        "}", login, password));
                break;
            case "Некорректные поля":
                request.withBody("key", "value");
                break;
        }
        request.withServiceUri(loginServiceURI).method(Method.PUT).execute();
        response = request.response();
    }

    public void loginWithCredentials(String login, String password) {
        if (login.equals("Пустой")) login = "";
        if (password.equals("Некорректный")) password = "asjfansjfasfnsaf";
        if (password.equals("Пустой")) password = "";
        loginPut(login, password, "Корректный", "Корректный");
    }

    public void loginWithContentType(String login, String password, String contentType) {
        loginPut(login, password, contentType, "Корректный");
    }

    public void loginWithBodyType(String login, Object password, String bodyType) {
        loginPut(login, password, "Корректный", bodyType);
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


}
