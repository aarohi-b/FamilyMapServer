package Services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadReq;
import Response.LoadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadServiceTest {
    LoadResponse result=new LoadResponse();
    LoadResponse result2=new LoadResponse();

    @BeforeEach
    public void setUp() throws DataAccessException {
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();

            UserDAO userDao = new UserDAO(conn);
            User u1 = new User("n1", "p1", "e1", "big", "boots","f","1234");
            User u2 = new User("n2", "p2", "e2", "big1", "boots","m","5678");

            PersonDAO personDao = new PersonDAO(conn);
            Person p1 = new Person("1", "n1", "a", "d", "f","1","1234", "3");
            Person p2 = new Person("2", "n1","b","e","f","11",null,"33");
            Person p3 = new Person("3", "n2","c","f","m",null,"222","333");

            Event e1 = new Event("id", "n1", "h", 100, 20,"india","delhi", "death", 1947);
            Event e2 = new Event("id2", "n1", "hh", 992, 11,"america","provo", "death", 1800);
            Event e3 = new Event("id3", "n1", "hhh", 44, 1304,"n","t", "birth", 1830);
            Event wrongEvent = new Event();//Loading info where one of the events is null, expected failure

            Event[] eventArray = new Event[] {e1, e2, e3};
            Person[] personArray = new Person[] {p1, p2, p3};
            User[] userArray = new User[] {u1, u2};
            LoadReq loadRequest = new LoadReq(userArray, personArray, eventArray);
            LoadService loadService = new LoadService();

            Event[] eventArray2 = new Event[] {e1, e2, e3, wrongEvent};
            LoadReq loadRequest2 = new LoadReq(userArray, personArray, eventArray2);

            result = loadService.load(loadRequest);
            result2 = loadService.load(loadRequest2);

            db.closeConnection(true);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void loadPass(){  //Loading test info into database
        assertEquals(result.getMessage(), "Successfully added 2 users, 3 persons, and 3 events to the database");
    }

    @Test
    public void loadFail(){
        assertEquals(result2.getMessage(), "Invalid input parameters");
    }
}
