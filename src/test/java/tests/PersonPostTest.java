package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PersonPostTest extends BaseTest {

    private static String generatePassport() {
        int seria = (int) (Math.random() * 9000 + 1000);
        int number = (int) (Math.random() * 900000 + 100000);
        return seria + "-" + number;
    }

    @DataProvider
    public static Object[][] negativeProvider() {
        return new Object[][]{
                {"operator", "operator", "", "QA_NAME", "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", null, "QA_NAME", "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Required data was not provided"},
                {"operator", "operator", 15, "QA_NAME", "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", "QA_SURNAME", "", "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", "QA_SURNAME", null, "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Required data was not provided"},
                {"operator", "operator", "QA_SURNAME", 55, "QA_LASTNAME", "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "", "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", null, "01.01.1970", generatePassport(), 400, "Required data was not provided"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", 77, "01.01.1970", generatePassport(), 400, "Incorrect Surname/Name/Lastname format."},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "1970-01-01", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01-01-1970", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "randomtext", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "30.02.1970", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "12.01", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.1970", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "40.01.1970", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "12.13.1970", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", 5, generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "", generatePassport(), 400, "Incorrect date format. Must be in 31.12.2018 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "text-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-txttxt", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "123-456789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-56789", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "12345-678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234-5678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "1234567890", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "12345678901", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", "123456789012", 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", 124124124, 400, "Passport format must be in 0000-000000 format"},
                {"operator", "operator", "QA_SURNAME", "QA_NAME", "QA_LASTNAME", "01.01.1970", 12512521512515L, 400, "Passport format must be in 0000-000000 format"},
        };
    }

    @Test(groups = {"needApiKey", "clearPossibleCreations"}, dataProvider = "negativeProvider")
    public void negativePostTest(String login, String password, Object surname, Object name, Object lastname,
                                 Object birthdate, Object passport, int expectedStatusCode, String expectedMessage) {
        app.person().personPost(apiKey, surname, name, lastname, birthdate, passport);

        Assert.assertEquals(app.person().response().statusCode(), expectedStatusCode);

        Assert.assertEquals(app.person().response().getBodyValue("message"), expectedMessage);
    }

    @BeforeMethod(onlyForGroups = {"clearPossibleCreations"})
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
