package dao;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken tok;
    private AuthToken badtok;
    private AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        tok = new AuthToken("user", "token");
        badtok = new AuthToken(null, null);

        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        aDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    //inserting a valid AuthToken, should return not null
    public void insertPass(){
        try {
            aDao.insert(tok);
            AuthToken compareTest = aDao.find(tok.getAuthTok());
            assertNotNull(compareTest);
            assertEquals(tok, compareTest);
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    //inserting an invalid AuthToken with null username and token, should throw exception
    public void insertEventFail(){
        assertThrows(Exception.class, ()->aDao.insert(badtok));
    }

    @Test
    //finding an existing token, should pass
    public void findPass() {
        try{
            aDao.insert(tok);
            AuthToken a = aDao.find("token");
            assertEquals(a, tok);
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Test
    //finding an authtoken that doesn't exist, should return null
    public void findFail() {
        try{
            assertNull(aDao.find("fail"));
        }
        catch (DataAccessException e){
            e.printStackTrace();
        }
    }
}
