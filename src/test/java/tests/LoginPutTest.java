package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPutTest extends BaseTest {

    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{
                {"user", "user", "123456789012"},
                {"operator", "operator", "111122223333"}
        };
    }

    @DataProvider
    public static Object[][] credentialsProvider() {
        return new Object[][] {
                {"user", "Некорректный", 401, "You are not authorized in the system"},
                {"user", "Пустой", 401, "You are not authorized in the system"},
                {"Пустой", "Некорректный", 401, "You are not authorized in the system"},
                {"Пустой", "user", 401, "You are not authorized in the system"},
        };
    }

    @DataProvider
    public static Object[][] contentTypeProvider() {
        return new Object[][]{
                {"user", "user", "Некорректный", "Incorrect Content-Type Header"},
                {"user", "user", "Пустой", "Incorrect Content-Type Header"},
                {"user", "user", "Отсутствует", "Incorrect Content-Type Header"},
        };
    }

    @DataProvider
    public static Object[][] bodyTypeProvider() {
        return new Object[][]{
                {"user", "user", "Без login", 400, "Required data was not provided"},
                {"user", "user", "Без password", 400, "Required data was not provided"},
                {"user", "user", "Пустой json", 400, "Required data was not provided"},
                {"user", "user", "Пустой json массив", 400, "Required data was not provided"},
                {"user", "user", "Формат не json", 400, "Incorrect format (not a json)"},
                {"user", "user", "Некорректные поля", 400, "Required data was not provided"},
                {"user", 515, "Значение int вместо String", 401, "You are not authorized in the system"},
        };
    }

    @Test(dataProvider = "positiveProvider")
    public void positiveLoginTest(String user, String password, String expectedApiKey) {
        app.auth().loginWithCredentials(user, password);

        Assert.assertEquals(app.auth().response().statusCode(), 200);

        Assert.assertEquals(app.auth().response().getBodyValue("api_Key"), expectedApiKey);
    }

    @Test(dataProvider = "credentialsProvider")
    public void differentCredentialsTest(String user, String password, int expectedStatusCode, String expectedErrorMessage) {
        app.auth().loginWithCredentials(user, password);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

    @Test(dataProvider = "contentTypeProvider")
    public void differentContentTypeTest(String user, String password, String contentType, String expectedErrorMessage) {
        app.auth().loginWithContentType(user, password, contentType);

        Assert.assertEquals(app.auth().response().statusCode(), 400);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

    @Test(dataProvider = "bodyTypeProvider")
    public void differentBodyTypeTest(String user, Object password, String bodyType, int expectedStatusCode, String expectedErrorMessage) {
        app.auth().loginWithBodyType(user, password, bodyType);

        Assert.assertEquals(app.auth().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

}
