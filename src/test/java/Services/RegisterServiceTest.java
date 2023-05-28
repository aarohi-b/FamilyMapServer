package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Request.RegisterReq;
import Response.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    RegisterResponse result = new RegisterResponse();
    RegisterResponse result2 = new RegisterResponse();
    RegisterResponse result3 = new RegisterResponse();

    @BeforeEach
    public void setUp() throws DataAccessException{
        Database db = new Database();
        try {
            ClearService clearService = new ClearService();
            clearService.clearDb();
            Connection conn = db.getConnection();
            RegisterService registerService = new RegisterService();
            RegisterReq regReq = new RegisterReq("user", "pass", "e", "J", "K","m");
            result=registerService.register(regReq);
            regReq.setGender(null);
            result2=registerService.register(regReq);
            RegisterReq falseReq = new RegisterReq("user", "pass", "e", "J", "K","m");
            RegisterService rs = new RegisterService();
            result3 = rs.register(falseReq);
        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    public void registerPass(){ //Using the services to register a new user
        assertNotNull(result.getPersonId());
        assertNotNull(result.getAuthTok());
        assertNotNull(result.getUserName());
        assertNull(result.getErrorMessage());
    }

    @Test
    public void registerFail(){ //Registering a new user with bad input
        assertNotNull(result2.getErrorMessage());
        assertEquals(result2.getErrorMessage(),"Error: Invalid Input");
        assertNull(result2.getAuthTok());
        assertNull(result2.getUserName());
        assertNull(result2.getPersonId());
    }

    @Test
    public void registerNewUserAlreadyExists() //Registering a new user that is already in the database
    {
        assertNotNull(result3.getErrorMessage());
        assertEquals(result3.getErrorMessage(),"Error: Username already exists");
        assertNull(result3.getAuthTok());
        assertNull(result3.getUserName());
        assertNull(result3.getPersonId());
    }
}
