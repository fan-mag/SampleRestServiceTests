package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPutTests extends BaseTest {

    @DataProvider
    public static Object[][] positiveProvider() {
        return new Object[][]{
                {"user", "user"},
                {"operator", "operator"}
        };
    }

    @Test(dataProvider = "positiveProvider")
    public void positiveLogin(String user, String password) {
        app.auth().login(user, password);

        System.out.println(app.auth().response().statusCode());
    }
}
