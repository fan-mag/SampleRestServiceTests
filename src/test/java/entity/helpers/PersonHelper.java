package entity.helpers;

import entity.builders.RequestBuilder;
import io.restassured.http.Method;

public class PersonHelper extends BaseHelper {
    private final String personServiceUri = properties.getProperty("personServiceURI");

    public void personGet(String apiKeyType, String apiKey, String surname, String name, String lastname, String id) {
        request = new RequestBuilder();
        setApiKey(apiKeyType, apiKey);
        switch (surname) {
            case "Отсутствует":
                break;
            default:
                request.withParam("surname", surname);
        }
        switch (name) {
            case "Отсутствует":
                break;
            default:
                request.withParam("name", name);
        }
        switch (lastname) {
            case "Отсутствует":
                break;
            default:
                request.withParam("lastname", lastname);
        }
        switch (id) {
            case "Отсутствует":
                break;
            default:
                request.withParam("id", id);
        }
        request.withServiceUri(personServiceUri).method(Method.GET).execute();
        response = request.response();
    }

    public void personGetNoParams(String apiKey) {
        personGet("Корректный", apiKey, "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует");
    }
}


