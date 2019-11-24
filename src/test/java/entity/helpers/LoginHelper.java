package entity.helpers;

import entity.builders.RequestBuilder;
import io.restassured.http.Method;


public class LoginHelper extends BaseHelper {
    private final String loginServiceURI = properties.getProperty("loginServiceURI");

    private void loginPut(String login, Object password, String contentType, String bodyType) {
        request = new RequestBuilder();
        setContentType(contentType);
        switch (bodyType) {
            case "Корректный":
                request.withBody("login", login);
                request.withBody("password", password);
                break;
            case "Значение int вместо String":
                request.withBody("login", login);
                request.withBody("password", 3253253);
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

    public void loginPutWithCredentials(String login, String password) {
        if (login.equals("Пустой")) login = "";
        if (password.equals("Некорректный")) password = "asjfansjfasfnsaf";
        if (password.equals("Пустой")) password = "";
        loginPut(login, password, "Корректный", "Корректный");
    }

    public void loginPutWithContentType(String login, String password, String contentType) {
        loginPut(login, password, contentType, "Корректный");
    }

    public void loginPutWithBodyType(String login, Object password, String bodyType) {
        loginPut(login, password, "Корректный", bodyType);
    }

    private void loginPost(String apiKey, String login, Object password, String contentType, String apiKeyType, String bodyType) {
        request = new RequestBuilder();
        setContentType(contentType);
        setApiKey(apiKeyType, apiKey);
        switch (bodyType) {
            case "Корректный":
                request.withBody("login", login);
                request.withBody("password", password);
                break;
            case "Значение int вместо String":
                request.withBody("login", login);
                request.withBody("password", 235253);
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
        request.withServiceUri(loginServiceURI).method(Method.POST).execute();
        response = request.response();
    }

    public void loginPostWithCredentials(String apiKey, String login, String password) {
        if (login.equals("Пустой")) login = "";
        if (password.equals("Некорректный")) password = "asjfansjfasfnsaf";
        if (password.equals("Пустой")) password = "";
        loginPost(apiKey, login, password, "Корректный", "Корректный", "Корректный");
    }


    public void loginPostWithContentType(String apiKey, String login, String password, String contentType) {
        loginPost(apiKey, login, password, contentType, "Корректный", "Корректный");
    }

    public void loginPostWithApiKey(String login, String password, String apiKeyType) {
        loginPost("--/--", login, password, "Корректный", apiKeyType, "Корректный");
    }

    public void loginPostWithBodyType(String apiKey, String login, Object password, String bodyType) {
        loginPost(apiKey, login, password, "Корректный", "Корректный", bodyType);
    }
}
