package tests;

import entity.model.Person;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

public class PersonGetTest extends BaseTest {


    @DataProvider
    public static Object[][] positiveProviderNoParams() {
        return new Object[][]{
                {app.user().login(), app.user().password()}
        };
    }

    @Test(groups = {"needApiKey"}, dataProvider = "positiveProviderNoParams")
    public void positivePersonGetNoParamTest(String login, String password) throws SQLException {
        app.person().personGetNoParams(apiKey);

        Assert.assertEquals(app.person().response().statusCode(), 200);

        List<Person> expectedPersons = app.db().getPerson("", "", "");
        List<Person> actualPersons = app.person().response().persons();

        Assert.assertEqualsNoOrder(expectedPersons.toArray(), actualPersons.toArray());
    }

}
