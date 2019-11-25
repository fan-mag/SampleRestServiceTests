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

    public void personDelete(String apiKey, Object id, Object surname, Object name, Object lastname, Object passport) {
        request = new RequestBuilder();
        setApiKey("Корректный", apiKey);
        setContentType("Корректный");
        if (!id.equals("Отсутствует"))
            request.withBody("id", id);
        if (!surname.equals("Отсутствует"))
            request.withBody("surname", surname);
        if (!name.equals("Отсутствует"))
            request.withBody("name", name);
        if (!lastname.equals("Отсутствует"))
            request.withBody("lastname", lastname);
        if (!passport.equals("Отсутствует"))
            request.withBody("passport", passport);
        request.withServiceUri(personServiceUri).method(Method.DELETE).execute();
        response = request.response();
    }

    public void personDelete(String apiKey, String body) {
        request = new RequestBuilder();
        setApiKey("Корректный", apiKey);
        setContentType("Корректный");
        request.withBody(body);
        request.withServiceUri(personServiceUri).method(Method.DELETE).execute();
        response = request.response();
    }

    public void personPost(String apiKey, Object surname, Object name, Object lastname, Object birthdate, Object passport) {
        request = new RequestBuilder();
        setApiKey("Корректный", apiKey);
        setContentType("Корректный");
        request.withBody("surname", surname);
        request.withBody("name", name);
        request.withBody("lastname", lastname);
        request.withBody("birthdate", birthdate);
        request.withBody("passport", passport);
        request.withServiceUri(personServiceUri).method(Method.POST).execute();
        response = request.response();
    }

    public void personPutWithParameters(String apiKey, Object id, Object surname, Object name, Object lastname, Object birthdate, Object passport) {
        request = new RequestBuilder();
        setApiKey("Корректный", apiKey);
        setContentType("Корректный");
        request.withBody("id", id);
        request.withBody("surname", surname);
        request.withBody("name", name);
        request.withBody("lastname", lastname);
        request.withBody("birthdate", birthdate);
        request.withBody("passport", passport);
        request.withServiceUri(personServiceUri).method(Method.PUT).execute();
        response = request.response();
    }
}


