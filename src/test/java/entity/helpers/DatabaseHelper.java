package entity.helpers;

import entity.model.Passport;
import entity.model.Person;
import entity.model.User;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private final String url;
    private Connection connection;

    DatabaseHelper(String url, String driver) throws ClassNotFoundException {
        Class.forName(driver);
        this.url = url;
    }

    void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    void disconnect() throws SQLException {
        connection.close();
    }

    public void addUser(User user) throws SQLException {
        String query = String.format("INSERT INTO credentials " +
                        "(login, password, api_key, privilege) " +
                        "VALUES ('%s', '%s', %d, %d)",
                user.login(), user.password(), user.apiKey(), user.privilege());
        allureDatabaseAttachment(query);
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    public void addPassport(Passport passport) throws SQLException {
        String query = String.format("INSERT INTO passport " +
                "(id, person_id, Серия, Номер) " +
                "VALUES (DEFAULT, '%s', %d, %d)",
                passport.person().id(), passport.seria(), passport.number());

    }

    public void deleteUser(User user) throws SQLException {
        String query = String.format("DELETE FROM credentials WHERE login = '%s'", user.login());
        allureDatabaseAttachment(query);
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }


    public Long getApiKey(User user) throws SQLException {
        String query = String.format("SELECT api_key FROM credentials WHERE login = '%s'", user.login());
        allureDatabaseAttachment(query);
        ResultSet rs = connection.createStatement().executeQuery(query);
        rs.next();
        Long apiKey = rs.getLong("api_key");
        rs.close();
        return apiKey;
    }

    public Person getPerson(Integer id) throws SQLException {
        String query = String.format("SELECT * FROM person WHERE id = %d", id);
        allureDatabaseAttachment(query);
        ResultSet rs = connection.createStatement().executeQuery(query);
        Person person = null;
        if (rs.next()) {
            person = new Person()
                    .withId(rs.getInt("id"))
                    .withSurname(rs.getString("Фамилия"))
                    .withName(rs.getString("Имя"))
                    .withLastname(rs.getString("Отчество"))
                    .withBirthdate(rs.getDate("Дата_рождения"));
        }
        return person;
    }

    public List<Person> getPerson(String surname, String name, String lastname) throws SQLException {
        String query = buildQueryPerson(surname, name, lastname);
        ResultSet rs = connection.createStatement().executeQuery(query);
        List<Person> personList = new ArrayList<>();
        while (rs.next()) {
            personList.add(new Person()
                    .withId(rs.getInt("id"))
                    .withSurname(rs.getString("Фамилия"))
                    .withName(rs.getString("Имя"))
                    .withLastname(rs.getString("Отчество"))
                    .withBirthdate(rs.getDate("Дата_рождения")));
        }
        return personList;
    }

    void addPerson(Person person) {
        String query = String.format("INSERT INTO person " +
                        "(id, Фамилия, Имя, Отчество, Дата_рождения) " +
                        "VALUES (DEFAULT, '%s', '%s', '%s', '%s') RETURNING id",
                person.surname(), person.name(), person.lastname(), person.birthdate());
        try (ResultSet rs = connection.createStatement().executeQuery(query);
        ) {
            rs.next();
            person.withId(rs.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deletePerson(Person person) {
        String query = String.format("DELETE FROM person " +
                        "WHERE id = %d OR Фамилия = '%s' OR Имя = '%s' OR Отчество = '%s'",
                person.id(), person.surname(), person.name(), person.lastname());
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String buildQueryPerson(String surname, String name, String lastname) {
        int caseInput = 7;
        if (surname == null || surname.equals("")) caseInput -= 4;
        if (name == null || name.equals("")) caseInput -= 2;
        if (lastname == null || lastname.equals("")) caseInput -= 1;
        switch (caseInput) {
            case 0:
                return String.format("SELECT * FROM person");
            case 1:
                return String.format("SELECT * FROM person WHERE Отчество = '%s'", lastname);
            case 2:
                return String.format("SELECT * FROM person WHERE Имя = '%s'", name);
            case 3:
                return String.format("SELECT * FROM person WHERE Имя = '%s' AND Отчество = '%s'", name, lastname);
            case 4:
                return String.format("SELECT * FROM person WHERE Фамилия = '%s'", surname);
            case 5:
                return String.format("SELECT * FROM person WHERE Фамилия = '%s' AND Отчество = '%s'", surname, lastname);
            case 6:
                return String.format("SELECT * FROM person WHERE Фамилия = '%s' AND Имя = '%s'", surname, name);
            case 7:
                return String.format("SELECT * FROM person WHERE Фамилия = '%s' AND Имя = '%s' AND Отчество = '%s'", surname, name, lastname);
        }
        return "THIS CASE CAN'T HAPPEN";
    }


    @Step("Отправление SQL-запроса в БД")
    private void allureDatabaseAttachment(String query) {
        Allure.attachment("SQL-запрос", query);
    }

    @Step("Поулченный ответ из БД")
    private void allureDatabaseAttachment(Integer result) {
        Allure.attachment("SQL-ответ", String.valueOf(result));
    }

}
