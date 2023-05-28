package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.User;
import Request.LoginReq;
import Response.FillResponse;
import Response.LoadResponse;
import Response.LoginResponse;

import java.sql.Connection;
import java.util.UUID;

/**
 *A Login Service object which generates a LoginResponse object
 */
public class LoginService {
    private AuthToken authToken = new AuthToken();
    private User userToFind;

    private Database myDB;

    public LoginService(){
        myDB = new Database();
    }

    public LoginResponse login(LoginReq logReq) {
        LoginResponse response = new LoginResponse();

        try {
            Connection conn = myDB.getConnection();
            AuthTokenDAO myADao = new AuthTokenDAO(conn);
            UserDAO myUDao = new UserDAO(conn);

            if (!checkLogin(logReq)) { //check that username and pass are not null
                response.setErrorMessage("Error: Invalid input");
            }

            userToFind = myUDao.find(logReq.getUsername());
            if (userToFind == null) {
                response.setErrorMessage("Error: User not found or does not exist");
            }
            else if (!userToFind.getPassword().equals(logReq.getPassword())) {
                response.setErrorMessage("Error: Incorrect Password");
            }
            else {
                authToken.setUsername(logReq.getUsername());
                authToken.setAuthTok(UUID.randomUUID().toString());
                myADao.insert(authToken);
                myDB.closeConnection(true);
                return new LoginResponse(authToken.getAuthTok(), userToFind.getUsername(), userToFind.getPersonID());
            }
            myDB.closeConnection(true);
        }
        catch (DataAccessException e){
            response.setSuccessFlag(false);
            response.setErrorMessage(e.getMessage());
            try{
                myDB.closeConnection(false);
            }catch (DataAccessException d){
                response.setSuccessFlag(false);
                response.setErrorMessage(d.getMessage());
            }
        }
        return response;
    }

    //returns true only when both are not null
    private boolean checkLogin(LoginReq logReq) {
        return !(logReq.getPassword() == null || logReq.getUsername() == null);
    }
}
