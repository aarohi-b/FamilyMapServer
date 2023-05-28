package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Response.FillResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import Response.PersonResponse;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private FillService myFillService;
    @BeforeEach
    public void setUp() throws DataAccessException {
        myFillService = new FillService();
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            authTokenDao.insert(new AuthToken("no", "1234"));
            authTokenDao.insert(new AuthToken("noUser", "10"));
            User testUser = new User("no", "pass", "email", "Jey", "Key","m","1234");
            UserDAO userDao = new UserDAO(conn);
            userDao.insert(testUser);
            db.closeConnection(true);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        myFillService = null;
    }

    @Test
    public void fillPass() {//Basic fill, testing to see if it inserts the whole family tree
        PersonService personService = new PersonService();
        PersonResponse allPersonResults = personService.everyPerson("1234");
        assertNull(allPersonResults.getData());
        assertEquals(allPersonResults.getMessage(), "Error: There are no people under user");

        FillService fillService = new FillService();
        FillResponse fillResult = fillService.fill("no", 4);
        assertNotEquals(fillResult.getMessage(), "Error: User not found!");
        assertNotEquals(fillResult.getMessage(), "Error: Number of Generations invalid");
        assertNotEquals(fillResult.getMessage(), "Error");

        allPersonResults = personService.everyPerson("1234");
        assertNotNull(allPersonResults.getData());
        assertEquals(allPersonResults.getData().size(), 31);
    }

    @Test
    public void fillFail() //Fill failing due to invalid input, a negative number of generations
    {
        PersonService personService = new PersonService();
        PersonResponse allPersonResults = personService.everyPerson("1234");
        assertNull(allPersonResults.getData());
        assertEquals(allPersonResults.getMessage(), "Error: There are no people under user");

        FillService fillService = new FillService();
        FillResponse fillResult = fillService.fill("no", -1);

        assertEquals(fillResult.getMessage(),"Error: Number of Generations invalid");
    }
}
