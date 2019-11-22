package entity.builders;


import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.lang.System.lineSeparator;


public class RequestBuilder {
    private String baseUri;
    private String serviceUri = "";
    private Method method = Method.GET;
    private List<Header> headers = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Object> body = new HashMap<>();
    private String bodyPlain;
    private RequestSpecification builtSpecification;
    private ResponseHandler response;
    private Boolean isBodyJson;

    public RequestBuilder(String baseUri) {
        this.baseUri = baseUri;

    }

    public RequestBuilder() {
        this(RestAssured.baseURI);
    }

    public RequestBuilder withServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
        return this;
    }

    public RequestBuilder withSubUri(String subUri) {
        this.serviceUri += "/" + subUri;
        return this;
    }

    public RequestBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder withHeader(Header header) {
        headers.add(header);
        return this;
    }

    public RequestBuilder withContentType() {
        return this.withHeader("Content-Type", "application/json");
    }

    public RequestBuilder withHeader(String name, Object value) {
        Header header = new Header(name, String.valueOf(value));
        return withHeader(header);
    }

    public RequestBuilder withParam(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public RequestBuilder withBody(String name, Object value) {
        body.put(name, value);
        isBodyJson = true;
        return this;
    }

    public RequestBuilder withBody(String plain) {
        bodyPlain = plain;
        isBodyJson = false;
        return this;
    }

    private RequestBuilder build() {
        EncoderConfig encConfig = EncoderConfig.encoderConfig().encodeContentTypeAs("application/json", ContentType.TEXT);
        RestAssuredConfig restConfig = RestAssured.config().encoderConfig(encConfig);
        Headers headers = new Headers(this.headers);

        builtSpecification = given()
                .config(restConfig)
                .baseUri(baseUri)
                .headers(headers)
                .params(params);
        if (method == Method.PUT || method == Method.POST || method == Method.DELETE) {
            String body = (isBodyJson) ? new BodyBuilder().buildBody(this.body) : new BodyBuilder().buildBody(bodyPlain);
            builtSpecification = builtSpecification.body(body);
        }
        return this;
    }

    @Step("Конструирование и отправление запроса")
    public RequestBuilder execute() {
        build();
        switch (method) {
            case GET:
                response = new ResponseHandler(get());
                break;
            case PUT:
                response = new ResponseHandler(put());
                break;
            case POST:
                response = new ResponseHandler(post());
                break;
            case DELETE:
                response = new ResponseHandler(delete());
                break;
        }
        allureRxAttachment();
        return this;
    }

    private Response put() {
        return builtSpecification.when().put(serviceUri);
    }

    private Response get() {
        return builtSpecification.when().get(serviceUri);
    }

    private Response post() {
        return builtSpecification.when().post(serviceUri);
    }

    private Response delete() {
        return builtSpecification.when().delete(serviceUri);
    }

    public ResponseHandler response() {
        return response;
    }


    public void allureRxAttachment() {
        Allure.addAttachment("Запрос", this.toString());
        Allure.addAttachment("Ответ", response.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("URI = ").append(baseUri).append(serviceUri).append(lineSeparator());
        sb1.append("METHOD = ").append(method).append(lineSeparator());
        sb1.append("HEADERS: ").append(lineSeparator());
        headers.forEach((header) -> sb1.append(header).append(lineSeparator()));
        sb1.append("PARAMETERS: ").append(lineSeparator());
        params.forEach((key, value) -> sb1.append(key).append("=").append(value).append(lineSeparator()));
        sb1.append("BODY: ").append(lineSeparator());
        body.forEach((key, value) -> sb1.append(key).append("=").append(value).append(lineSeparator()));
        return sb1.toString();
    }
}
