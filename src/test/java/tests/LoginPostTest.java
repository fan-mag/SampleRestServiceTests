package tests;

import entity.model.User;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class LoginPostTest extends BaseTest {
    private User user = new User()
            .withLogin("QA_Login_Post")
            .withPassword("QA_Login_post")
            .withApiKey(101010101010L)
            .withPrivilege(5);

    @BeforeClass
    public void setUpLoginPostTests() throws SQLException {
        app.db().addUser(user);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownLoginPostTests() throws SQLException {
        app.db().deleteUser(user);
    }

    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{
                {"QA_Login_Post", "QA_Login_post"}
        };
    }

    @Test(groups = {"needApiKey"}, dataProvider = "positiveProvider")
    public void positiveLoginPostTest(String login, String password) throws SQLException {
        Long oldKey = user.apiKey();

        app.auth().loginPostWithCredentials(apiKey, login, password);

        Assert.assertEquals(app.auth().response().statusCode(), 202);

        Long actualNewKey = app.db().getApiKey(user);

        Assert.assertNotEquals(oldKey, actualNewKey);

        Assert.assertEquals(app.auth().response().getBodyValue("api_Key"), actualNewKey.toString());
    }


}
