package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Response.PersonIDResponse;
import Response.PersonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    @BeforeEach
    public void setUp() throws DataAccessException {
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            PersonDAO personDao = new PersonDAO(conn);
            authTokenDao.insert(new AuthToken("no", "1234"));
            authTokenDao.insert(new AuthToken("noUser", "10"));
            Person testPerson = new Person("IDD", "no", "A", "B", "m","m","2222", "2312");
            personDao.insert(testPerson);

            Person test2 = new Person("111", "no","A","B","f","1","2","3");
            Person test3 = new Person("123", "no","Tiare","Johnson.","m","sww","abc","kdd");
            Person test4 = new Person("100", "new","Jen","aa","m","45","aabbcc","4555");
            personDao.insert(test2);
            personDao.insert(test3);
            personDao.insert(test4);

            User testUser = new User("no", "pass", "email", "Jey", "Key","m","idPerson");
            UserDAO userDao = new UserDAO(conn);
            userDao.insert(testUser);
            db.closeConnection(true);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void singlePass(){ //Find a single person from the database
        PersonService personService = new PersonService();
        PersonIDResponse result = personService.singlePerson("IDD", "1234");

        assertNotNull(result.getAssociatedUsername());
        assertNotNull(result.getFatherID());
        assertNotNull(result.getFirstName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getGender());
        assertEquals(result.getAssociatedUsername(), "no");
    }

    @Test
    public void singlePersonFail() { //Find a single person without a valid auth token
        PersonService personService = new PersonService();
        PersonIDResponse result = personService.singlePerson("yes", "123");

        assertNull(result.getAssociatedUsername());
        assertNull(result.getFatherID());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getGender());
        assertNotNull(result.getErrorMessage());
        assertEquals(result.getErrorMessage(), "Error: Invalid Authorization Token");
    }

    @Test
    public void allPersons() //Finding all people under a user
    {
        PersonService personService = new PersonService();
        PersonResponse personResults = personService.everyPerson("1234");

        assertNotNull(personResults.getData());
        assertEquals(personResults.getData().size(), 3);
    }

    @Test
    public void allPersonsFail() //Finding no one under a user without any people
    {
        PersonService personService = new PersonService();
        PersonResponse personResults = personService.everyPerson("10");

        assertNull(personResults.getData());
        assertNotNull(personResults.getMessage());
        assertEquals(personResults.getMessage(), "Error: There are no people under user");
    }

}
