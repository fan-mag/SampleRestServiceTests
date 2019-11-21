package entity.helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.*;

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

    private ResultSet executeSel(String query) throws SQLException {
        allureDatabaseAttachment(query);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs;
    }

    private void executeDel(String query) throws SQLException {
        allureDatabaseAttachment(query);
        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    private Integer executeAdd(String query) throws SQLException {
        allureDatabaseAttachment(query);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        Integer id = rs.getInt("id");
        statement.close();
        allureDatabaseAttachment(id);
        return id;
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
