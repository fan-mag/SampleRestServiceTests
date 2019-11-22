package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class LoginPostTest extends BaseTest {
    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{
                {app.user2().login(), app.user2().password()}
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
    public static Object[][] apiKeyProvider() {
        return new Object[][]{
                {app.user().login(), app.user().password(), "Некорректный", 401, "Invalid API key header provided"},
                {app.user().login(), app.user().password(), "Пустой", 401, "Empty API key header provided"},
                {app.user().login(), app.user().password(), "Отсутствует", 401, "No API key header provided"},
                {app.user().login(), app.user().password(), "Текст", 401, "Incorrect type API key header provided"}
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

    @Test(groups = {"needApiKey"}, dataProvider = "positiveProvider")
    public void positiveLoginPostTest(String login, String password) throws SQLException {
        Long oldKey = app.user2().apiKey();

        app.auth().loginPostWithCredentials(apiKey, login, password);

        Assert.assertEquals(app.auth().response().statusCode(), 202);

        Long actualNewKey = app.db().getApiKey(app.user2());

        Assert.assertNotEquals(oldKey, actualNewKey);

        Assert.assertEquals(app.auth().response().getBodyValue("api_Key"), actualNewKey.toString());
    }

    @Test(groups = {"needApiKey"}, dataProvider = "contentTypeProvider")
    public void differentContentTypeTest(String login, String password, String contentType, String expectedMessage) throws SQLException {

        Long oldKey = app.user().apiKey();

        app.auth().loginPostWithContentType(apiKey, login, password, contentType);

        Assert.assertEquals(app.auth().response().statusCode(), 400);

        Long newKey = app.db().getApiKey(app.user());

        Assert.assertEquals(oldKey, newKey);
    }

    @Test(dataProvider = "apiKeyProvider")
    public void differentApiKeyTypeTest(String login, String password, String apiKeyType, int expectedStatusCode, String expectedMessage) throws SQLException {
        Long oldKey = app.user().apiKey();

        app.auth().loginPostWithApiKey(login, password, apiKeyType);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);
        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedMessage);

        Long newKey = app.db().getApiKey(app.user());

        Assert.assertEquals(oldKey, newKey);
    }

    @Test(groups = {"needApiKey"}, dataProvider = "bodyTypeProvider")
    public void differentBodyTypeTest(String login, Object password, String bodyType, int expectedStatusCode, String expectedErrorMessage) throws SQLException {
        Long oldKey = app.user().apiKey();

        app.auth().loginPostWithBodyType(apiKey, login, password, bodyType);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);
        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);

        Long newKey = app.db().getApiKey(app.user());

        Assert.assertEquals(oldKey, newKey);
    }
}
