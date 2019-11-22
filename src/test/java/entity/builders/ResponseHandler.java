package entity.builders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.model.Person;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.System.lineSeparator;

public class ResponseHandler {
    private Response response;

    ResponseHandler(Response response) {
        this.response = response;
    }

    public List<Header> getHeaders() {
        return response.headers().asList();
    }

    public String getHeaderValue(String key) {
        return response.header(key);
    }

    public String getCookie() {
        return getHeaderValue("Set-Cookie");
    }

    public int statusCode() {
        return response.getStatusCode();
    }

    public Object getBodyValue(String key) {
        return response.jsonPath().get(key);
    }

    public ResponseBodyData getBody() {
        return response.getBody();
    }

    public List<Person> persons() {
        String jsonString = getBody().asString();
        Type itemsListType = new TypeToken<List<Person>>(){}.getType();
        return new Gson().fromJson(jsonString, itemsListType);
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("STATUS CODE: ").append(statusCode()).append(lineSeparator());
        sb1.append("HEADERS: ").append(lineSeparator());
        getHeaders().forEach((header) -> sb1.append(header).append(lineSeparator()));
        sb1.append("BODY: ").append(lineSeparator());
        sb1.append(getBody().asString());
        return sb1.toString();
    }



}
