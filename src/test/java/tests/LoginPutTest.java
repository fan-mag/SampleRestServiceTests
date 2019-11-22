package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPutTest extends BaseTest {

    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{
                {app.user().login(), app.user().password(), app.user().apiKey().toString()},
                {app.operator().login(), app.operator().password(), app.operator().apiKey().toString()}
        };
    }

    @DataProvider
    public static Object[][] credentialsProvider() {
        return new Object[][]{
                {app.user().login(), "Некорректный", 401, "You are not authorized in the system"},
                {app.user().login(), "Пустой", 401, "You are not authorized in the system"},
                {"Пустой", "Некорректный", 401, "You are not authorized in the system"},
                {"Пустой", app.user().password(), 401, "You are not authorized in the system"},
        };
    }

    @DataProvider
    public static Object[][] contentTypeProvider() {
        return new Object[][]{
                {app.user().login(), app.user().password(), "Некорректный", "Incorrect Content-Type Header"},
                {app.user().login(), app.user().password(), "Пустой", "Incorrect Content-Type Header"},
                {app.user().login(), app.user().password(), "Отсутствует", "Incorrect Content-Type Header"},
        };
    }

    @DataProvider
    public static Object[][] bodyTypeProvider() {
        return new Object[][]{
                {app.user().login(), app.user().password(), "Без login", 400, "Required data was not provided"},
                {app.user().login(), app.user().password(), "Без password", 400, "Required data was not provided"},
                {app.user().login(), app.user().password(), "Пустой json", 400, "Required data was not provided"},
                {app.user().login(), app.user().password(), "Пустой json массив", 400, "Required data was not provided"},
                {app.user().login(), app.user().password(), "Формат не json", 400, "Incorrect format (not a json)"},
                {app.user().login(), app.user().password(), "Некорректные поля", 400, "Required data was not provided"},
                {app.user().login(), app.user().password(), "Значение int вместо String", 401, "You are not authorized in the system"},
        };
    }

    @Test(dataProvider = "positiveProvider")
    public void positiveLoginTest(String user, String password, String expectedApiKey) {
        app.auth().loginPutWithCredentials(user, password);

        Assert.assertEquals(app.auth().response().statusCode(), 200);

        Assert.assertEquals(app.auth().response().getBodyValue("api_Key"), expectedApiKey);
    }

    @Test(dataProvider = "credentialsProvider")
    public void differentCredentialsTest(String user, String password, int expectedStatusCode, String expectedErrorMessage) {
        app.auth().loginPutWithCredentials(user, password);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

    @Test(dataProvider = "contentTypeProvider")
    public void differentContentTypeTest(String user, String password, String contentType, String expectedErrorMessage) {
        app.auth().loginPutWithContentType(user, password, contentType);

        Assert.assertEquals(app.auth().response().statusCode(), 400);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

    @Test(dataProvider = "bodyTypeProvider")
    public void differentBodyTypeTest(String user, Object password, String bodyType, int expectedStatusCode, String expectedErrorMessage) {
        app.auth().loginPutWithBodyType(user, password, bodyType);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }


}
