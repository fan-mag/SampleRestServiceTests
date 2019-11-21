package entity.helpers;

import io.restassured.RestAssured;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationManager {
    private static final Properties properties = new Properties();
    private static final List<String> browsers = new ArrayList<>();
    private LoginHelper login;
    private DatabaseHelper db;


    public List<String> browsers() {
        return browsers;
    }




    static Properties properties() {
        return properties;
    }

    public void init() throws IOException, SQLException, ClassNotFoundException {
        properties.load(new FileReader("src/test/resources/testsuite.properties"));
        RestAssured.baseURI = property("baseURI");
        login = new LoginHelper();
        db = new DatabaseHelper(property("databaseURI"), property("databaseDriver"));
        db.connect();
    }

    public LoginHelper auth() {
        return login;
    }

    public void stop() throws SQLException {
        properties.clear();
        db.disconnect();
    }

    private String property(String key) {
        return properties.getProperty(key);
    }

    public DatabaseHelper db() {
        return db;
    }

}
