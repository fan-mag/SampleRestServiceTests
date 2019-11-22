package entity.helpers;

import entity.model.Person;
import entity.model.User;
import io.restassured.RestAssured;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationManager {
    private static final Properties properties = new Properties();
    private static final List<String> browsers = new ArrayList<>();
    private LoginHelper loginHelper;
    private PersonHelper personHelper;
    private DatabaseHelper db;
    private boolean isDebug = true;
    private User user, user2, operator;
    private List<Person> personList = new ArrayList<>();

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

    public void init() throws IOException, SQLException, ClassNotFoundException, ParseException {
        properties.load(new FileReader("src/test/resources/testsuite.properties"));
        RestAssured.baseURI = (isDebug) ? property("debugBaseURI") : property("baseURI");
        loginHelper = new LoginHelper();
        personHelper = new PersonHelper();
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

        for (int i = 1; i <= 10; i++) {
            String[] split = property(String.format("qa_person_%d", i)).split(";");
            personList.add(new Person()
                    .withSurname(split[0])
                    .withName(split[1])
                    .withLastname(split[2])
                    .withBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse(split[3])));
        }
        addPersonsToDatabase();
    }

    private void addUserToDatabase() throws SQLException {
        db.addUser(user);
        db.addUser(user2);
        db.addUser(operator);
    }

    private void deleteUserFromDatabase() throws SQLException {
        db.deleteUser(user);
        db.deleteUser(user2);
        db.deleteUser(operator);
    }

    private void addPersonsToDatabase() {
        personList.forEach(db::addPerson);
    }

    private void deletePersonsFromDatabase() {
        personList.forEach(db::deletePerson);

    }

    public LoginHelper auth() {
        return loginHelper;
    }

    public void stop() throws SQLException {
        properties.clear();
        deleteUserFromDatabase();
        deletePersonsFromDatabase();
        db.disconnect();
    }

    private String property(String key) {
        return properties.getProperty(key);
    }

    public DatabaseHelper db() {
        return db;
    }

    public PersonHelper person() {
        return personHelper;
    }
}
