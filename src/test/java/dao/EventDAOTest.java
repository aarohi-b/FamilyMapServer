package dao;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDAO;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event testEvent;
    private Event eventTwo;
    private Event eventThree;
    private Event eventFour;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException{
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        testEvent = new Event("eventID", "user", "a", 100, 20,"c","city", "death", 1969);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDAO(conn);
        eventTwo = new Event("id2", "user", "b", 10, 3,"m","p", "death", 1999);
        eventThree = new Event("id3", "no", "c", 999, 31,"n","q", "death", 1900);
        eventFour = new Event("id4", "no", "d", 212, 90,"o","r", "birth", 1970);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass(){
        try {
            eDao.insert(testEvent);     //inserting valid event
            Event compareTest = eDao.find(testEvent.getEventID());  //inserted event is found
            assertNotNull(compareTest); //found event is not null
            assertEquals(testEvent, compareTest); //found event is equal to inserted event
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    //throws exception when trying to insert already existing event
    public void insertEventFail(){
        try {
            eDao.insert(testEvent);
            assertThrows(Exception.class, ()->eDao.insert(testEvent));
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    public void findPass() {
        try{
            eDao.insert(testEvent);
            Event e = eDao.find("eventID");
            assertEquals(e, testEvent);
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    public void findFail() { //returns null when finding an event with wrong eventID
        try{
            assertNull(eDao.find("fail"));
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    public void selectAllPass(){ //list of selected events
        Event[] eventsArray=new Event[2];
        eventsArray[0]=testEvent;
        eventsArray[1]=eventTwo;

        try {
            eDao.insert(testEvent);
            eDao.insert(eventTwo);
            eDao.insert(eventFour);
            eDao.insert(eventThree);
            Event[] check = eDao.selectAllEvents("user");

            for (int i = 0; i < eventsArray.length; i++) {
                assertEquals(eventsArray[i], check[i]);
            }
        }
        catch (DataAccessException d){
            d.printStackTrace();
        }
    }

    @Test
    public void selectAllFail() //wrong eventID
    {
        try {
            eDao.insert(testEvent);
            eDao.insert(eventTwo);
            eDao.insert(eventFour);
            eDao.insert(eventThree);
            assertNull(eDao.selectAllEvents("fail"));
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deletePass(){ //only has a pass test because it deletes event if it exists, otherwise nothing
        try {
            eDao.insert(testEvent);
            eDao.deleteEventFromUser("user");
            assertNull(eDao.find("eventID"));
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }
}
