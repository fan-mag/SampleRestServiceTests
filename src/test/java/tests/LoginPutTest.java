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
                {"user", "user", "Без login", "Required data was not provided"},
                {"user", "user", "Без password", "Required data was not provided"},
                {"user", "user", "Пустой json", "Required data was not provided"},
                {"user", "user", "Пустой json массив", "Required data was not provided"},
                {"user", "user", "Формат не json", "Incorrect format (not a json)"},
                {"user", "user", "Некорректные поля", "Required data was not provided"},
        };
    }

    @Test(dataProvider = "positiveProvider")
    public void positiveLoginTest(String user, String password, String expectedApiKey) {
        app.auth().loginWithCredentials(user, password);

        Assert.assertEquals(app.auth().response().statusCode(), 200);

        Assert.assertEquals(app.auth().response().getBodyValue("api_Key"), expectedApiKey);
    }

    @Test(dataProvider = "contentTypeProvider")
    public void differentContentTypeTest(String user, String password, String contentType, String expectedErrorMessage) {
        app.auth().loginWithContentType(user, password, contentType);

        Assert.assertEquals(app.auth().response().statusCode(), 400);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

    @Test(dataProvider = "bodyTypeProvider")
    public void differentBodyTypeTest(String user, String password, String bodyType, String expectedErrorMessage) {
        app.auth().loginWithBodyType(user, password, bodyType);

        Assert.assertEquals(app.auth().response().statusCode(), 400);

        Assert.assertEquals(app.auth().response().getBodyValue("message"), expectedErrorMessage);
    }

}
