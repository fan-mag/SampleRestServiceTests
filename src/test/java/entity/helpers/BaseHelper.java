package entity.helpers;

import entity.builders.RequestBuilder;
import entity.builders.ResponseHandler;

import java.util.Properties;

public class BaseHelper {
    static final Properties properties = ApplicationManager.properties();
    ResponseHandler response;
    RequestBuilder request = new RequestBuilder();

    public ResponseHandler response() {
        return response;
    }

    void setApiKey(String apiKeyType, String apiKey) {
        switch (apiKeyType) {
            case "Текст":
                request.withHeader("Api-Key", "текст");
                break;
            case "Корректный":
                request.withHeader("Api-Key", apiKey);
                break;
            case "Некорректный":
                request.withHeader("Api-Key", "120492149520");
                break;
            case "Пустой":
                request.withHeader("Api-Key", "");
                break;
            case "Отсутствует":
                break;
        }
    }

    void setContentType(String contentType) {
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
    }
}
