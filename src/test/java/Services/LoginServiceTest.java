package Services;

import DataAccess.*;
import Model.User;
import Request.LoginReq;
import Response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    LoginResponse result = new LoginResponse();
    @BeforeEach
    public void setUp() throws DataAccessException {
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();
            UserDAO userDao = new UserDAO(conn);
            User testUser = new User("user", "pass", "a", "J", "P","m","1234");
            userDao.insert(testUser);
            db.closeConnection(true);
            LoginService loginService = new LoginService();
            result = loginService.login(new LoginReq("user", "pass"));

        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void loginPass(){ //Login with existing user
        assertTrue(result.getUserName().equals("user"));
        assertNull(result.getErrorMessage());
        assertNotNull(result.getPersonId());
        assertNotNull(result.getAuthTok());
    }

    @Test
    public void loginFail(){ //login when given incorrect password
        LoginService loginService = new LoginService();
        LoginResponse result = loginService.login(new LoginReq("user", "badpass"));

        assertNull(result.getUserName());
        assertNotNull(result.getErrorMessage());
        assertNull(result.getPersonId());
        assertNull(result.getAuthTok());
        assertTrue(result.getErrorMessage().equals("Error: Incorrect Password"));
    }
}