package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.EventReq;
import Response.EventIDResponse;
import Response.EventResponse;
import com.sun.jdi.request.EventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    @BeforeEach
    public void setUp() throws DataAccessException {
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();
            EventDAO eventDAO = new EventDAO(conn);
            Event e1 = new Event("id1", "no", "p1", 122, 2122,"India","Delhi", "death", 1969);
            Event e2 = new Event("id2", "no", "p2", 999, 2333,"America","Provo", "birth", 1920);
            Event e3 = new Event("id3", "no", "p3", 111, 463,"Pak","22111", "birth", 1890);
            Event e4 = new Event("id4", "yes", "p4", 2391, 54,"Iran","222", "death", 1933);
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            authTokenDao.insert(new AuthToken("no", "1234"));
            authTokenDao.insert(new AuthToken("username", "1010"));
            eventDAO.insert(e1);
            eventDAO.insert(e2);
            eventDAO.insert(e3);
            eventDAO.insert(e4);
            db.closeConnection(true);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
    @Test
    public void retrieveEventsPass(){
        EventService eventService = new EventService();
        EventReq r=new EventReq("1234") {};
        EventResponse result = eventService.retrieveEvents(r);
        assertNotNull(result.getData());
        assertEquals(result.getData().length, 3);
        assertNull(result.getMessage());
    }
    @Test
    public void retrieveEventsFail(){
        EventService eventService = new EventService();
        EventReq r = new EventReq("10");
        EventResponse result = eventService.retrieveEvents(r);

        assertNull(result.getData());
        assertNotNull(result.getMessage());
        assertEquals(result.getMessage(), "Error: Invalid AuthToken");
    }
    @Test
    public void retrieveEventPass(){
        EventIDService eventService = new EventIDService();
        EventIDResponse testSingleResult = eventService.retrieveEvent("id1", "1234");

        assertNotNull(testSingleResult.getAssociatedUsername());
        assertNotNull(testSingleResult.getCity());
        assertNotNull(testSingleResult.getCountry());
        assertNotNull(testSingleResult.getEventType());
        assertNotNull(testSingleResult.getEventID());
        assertEquals(1969, testSingleResult.getYear());
        assertEquals(122, testSingleResult.getLatitude());
        assertEquals(2122, testSingleResult.getLongitude(), .5);
        assertNull(testSingleResult.getMessage());
        assertEquals(testSingleResult.getAssociatedUsername(), "no");
    }

    @Test
    public void retrieveEventFail(){
        EventIDService eventService = new EventIDService();
        EventIDResponse singleEventResult = eventService.retrieveEvent("wrongID", "1234");

        assertNull(singleEventResult.getAssociatedUsername());
        assertNull(singleEventResult.getCity());
        assertNull(singleEventResult.getCountry());
        assertNull(singleEventResult.getEventType());
        assertNull(singleEventResult.getEventID());
        assertEquals(singleEventResult.getYear(), 0);
        assertEquals(0, singleEventResult.getLatitude(), .1);
        assertEquals(0, singleEventResult.getLongitude(), .1);
        assertNull(singleEventResult.getAssociatedUsername());
    }
}
