package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class PersonPutTest extends BaseTest {

    @DataProvider
    public static Object[][] securityProvider() {
        return new Object[][]{
                {"operator", "operator", 99999, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 404, "User with ID 99999 not found"},
                {"operator", "operator", 0, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 404, "User with ID 0 not found"},
                {"operator", "operator", -1, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 404, "User with ID -1 not found"},
                {"operator", "operator", "Text", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Incorrect data type"},
                {"operator", "operator", "99999", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 404, "User with ID 99999 not found"},
                {"operator", "operator", null, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Required data was not provided"},
                {"operator", "operator", 12345678, "", "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, 34, "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, null, "QA_NAME", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Required data was not provided"},
                {"operator", "operator", 12345678, "QA_SURNAME", "", "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, "QA_SURNAME", 34, "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, "QA_SURNAME", null, "QA_LASTNAME", "01.01.1970", "9999-999999", 400, "Required data was not provided"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "", "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", 34, "01.01.1970", "9999-999999", 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", null, "01.01.1970", "9999-999999", 400, "Required data was not provided"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "1970-01-01", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01-01-1970", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "randomtext", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "30.02.1970", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "12.01", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.1970", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "40.01.1970", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "12.13.1970", "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", 5, "9999-999999", 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", null, "9999-999999", 400, "Required data was not provided"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "text-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-txttxt", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "123-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-56789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "12345-678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-5678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234567890", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "12345678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "123456789012", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", 124124124, 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", 12345678, "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", 12512521512515L, 400, "Passport format must be in 0000-000000 format"},
        };
    }

    @BeforeMethod(onlyForGroups = "makeTestPerson")
    public void setUp(Object[] testArgs) throws SQLException {
        Integer id = (Integer) testArgs[2];
        app.db().addPerson(id);
    }

    @AfterMethod(onlyForGroups = "makeTestPerson")
    public void tearDown(Object[] testArgs) throws SQLException {
        Integer id = (Integer) testArgs[2];
        app.db().deletePerson(id);
    }

    @Test(dataProvider = "securityProvider", groups = {"needApiKey"})
    public void securityPersonPutTest(String login, String password, Object id, Object surname, Object name,
                                      Object lastname, Object birthdate, Object passport, int expectedStatusCode, String expectedMessage) {

        app.person().personPutWithParameters(apiKey, id, surname, name, lastname, birthdate, passport);

        Assert.assertEquals(app.person().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.person().response().getBodyValue("message"), expectedMessage);
    }


}
