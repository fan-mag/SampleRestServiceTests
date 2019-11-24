package tests;

import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PersonDeleteTest extends BaseTest {

    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{};
    }

    @DataProvider
    public static Object[][] negativeProvider() {
        return new Object[][]{
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 422, "You can't use this method without parameters"},
                {"operator", "operator", 999999, "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", 0, "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", -1, "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", "Text", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 400, "Incorrect data type"},
                {"operator", "operator", "Отсутствует", 25, "Отсутствует", "Отсутствует", "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", "Отсутствует", "Отсутствует", 25, "Отсутствует", "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", 25, "Отсутствует", 404, "No persons matched for your request"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "text-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "1234-txttxt", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "123-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "1234-56789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "12345-678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "1234-5678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "1234567890", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "12345678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", "123456789012", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 124124124, 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "Отсутствует", "Отсутствует", "Отсутствует", "Отсутствует", 12512521512515L, 400, "Passport format must be in 0000-000000 format"},
        };
    }

    @DataProvider
    public static Object[][] hackedJson() {
        return new Object[][]{
                {"operator", "operator", "{\"passport\":55 \"name\":\"safasf\"}", 400, "Incorrect format (not a json)"},
                {"operator", "operator", "dfgsdgfd", 400, "Incorrect format (not a json)"},
        };
    }


    @Test(groups = {"needApiKey"}, dataProvider = "negativeProvider")
    public void negativePersonDeleteTest(String user, String password, Object id, Object surname, Object name, Object lastname, Object passport, int expectedStatusCode, String expectedMessage) {
        app.person().personDelete(apiKey, id, surname, name, lastname, passport);

        Assert.assertEquals(app.person().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.person().response().getBodyValue("message"), expectedMessage);
    }

    @Test(groups = {"needApiKey"}, dataProvider = "hackedJson")
    public void hackedJsonTest(String user, String password, String body, int expectedStatusCode, String expectedMessage) {
        app.person().personDelete(apiKey, body);

        Assert.assertEquals(app.person().response().statusCode(), expectedStatusCode);
        
        Assert.assertEquals(app.person().response().getBodyValue("message"), expectedMessage);
    }
}
