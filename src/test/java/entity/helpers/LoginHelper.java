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


    public void loginPost(String apiKey, String login, String password, String contentType, String apiKeyType, String bodyType) {
        request = new RequestBuilder();
        switch (contentType) {
            case "Корректный":
                request.withHeader("Content-Type", "application/json");
                break;
        }
        switch (apiKeyType) {
            case "Корректный":
                request.withHeader("Api-Key", apiKey);
                break;
        }
        switch (bodyType) {
            case "Корректный":
                request.withBody("login", login);
                request.withBody("password", password);
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


}
