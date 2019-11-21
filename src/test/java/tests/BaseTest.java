package tests;

import entity.helpers.ApplicationManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.sql.SQLException;


public class BaseTest {
    public static ApplicationManager app;



    @BeforeSuite
    public void prepareTestSuite() throws IOException, SQLException, ClassNotFoundException {
        app = new ApplicationManager();
        app.init();
    }

    @AfterSuite(alwaysRun = true)
    public void returnToInitialState() throws SQLException {
        app.stop();
    }

}
