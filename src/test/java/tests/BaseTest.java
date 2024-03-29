package tests;

import entity.helpers.ApplicationManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


public class BaseTest {
    static ApplicationManager app;
    String apiKey;

    @BeforeMethod(onlyForGroups = "needApiKey")
    public void getApiKey(Object[] testArgs) {
        String login = (String) testArgs[0];
        String password = (String) testArgs[1];
        app.auth().loginPutWithCredentials(login, password);
        apiKey = (String) app.auth().response().getBodyValue("api_Key");
    }

    @BeforeSuite
    public void prepareTestSuite() throws IOException, SQLException, ClassNotFoundException, ParseException {
        app = new ApplicationManager();
        app.init();
    }

    @AfterSuite(alwaysRun = true)
    public void returnToInitialState() throws SQLException {
        app.stop();
    }

    @AfterMethod(onlyForGroups = {"clearPossibleCreations"})
    public void deleteUserFromDatabase(Object[] testArgs) {
        try {
            String surname = (String) testArgs[2];
            String name = (String) testArgs[3];
            String lastname = (String) testArgs[4];
            app.db().deletePerson(surname, name, lastname);
            String seria = ((String) testArgs[6]).split("-")[0];
            String number = ((String) testArgs[6]).split("-")[1];
            app.db().deletePassport(seria, number);
        } catch (Exception ignore) {

        }
    }
}
