package entity.helpers;

import entity.model.User;
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
    private boolean isDebug = true;
    private User user, user2, operator;
    public List<String> browsers() {
        return browsers;
    }

    public User user() {
        return user;
    }

    public User user2() {
        return user2;
    }

    public User operator() {
        return operator;
    }

    static Properties properties() {
        return properties;
    }

    public void init() throws IOException, SQLException, ClassNotFoundException {
        properties.load(new FileReader("src/test/resources/testsuite.properties"));
        RestAssured.baseURI = (isDebug) ? property("debugBaseURI") : property("baseURI");
        login = new LoginHelper();
        db = new DatabaseHelper(property("databaseURI"), property("databaseDriver"));
        db.connect();
        user = new User()
                .withLogin(property("user_login"))
                .withPassword(property("user_password"))
                .withApiKey(Long.parseLong(property("user_key")))
                .withPrivilege(Integer.parseInt(property("user_privilege")));
        user2 = new User()
                .withLogin(property("user_2_login"))
                .withPassword(property("user_2_password"))
                .withApiKey(Long.parseLong(property("user_2_key")))
                .withPrivilege(Integer.parseInt(property("user_2_privilege")));
        operator = new User()
                .withLogin(property("operator_login"))
                .withPassword(property("operator_password"))
                .withApiKey(Long.parseLong(property("operator_key")))
                .withPrivilege(Integer.parseInt(property("operator_privilege")));
        addUserToDatabase();
    }

    public void addUserToDatabase() throws SQLException {
        db.addUser(user);
        db.addUser(user2);
        db.addUser(operator);
    }

    public void deleteUserFromDatabase() throws SQLException{
        db.deleteUser(user);
        db.deleteUser(user2);
        db.deleteUser(operator);
    }

    public LoginHelper auth() {
        return login;
    }

    public void stop() throws SQLException {
        properties.clear();
        deleteUserFromDatabase();
        db.disconnect();
    }

    private String property(String key) {
        return properties.getProperty(key);
    }

    public DatabaseHelper db() {
        return db;
    }

}
