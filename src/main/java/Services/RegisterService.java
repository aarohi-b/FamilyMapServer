package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.RegisterReq;
import Response.FillResponse;
import Response.RegisterResponse;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

/**
 * A Register Service object which generates a RegisterResponse object
 */
public class RegisterService {
    private Database myDB=new Database();
    private Random random = new Random();
    private Names nameGenerator = new Names();
    private ArrayList<Person> familyTree=new ArrayList<>();
    private ArrayList<Event> allEvents = new ArrayList<>();
    private AuthToken authToken = new AuthToken();
    private User registerUser = new User();
    private Person registerPerson = new Person();
    private int defaultNumOfGenerations = 4;

    public RegisterResponse register(RegisterReq regReq) {
        RegisterResponse response = new RegisterResponse();
        if (!checkInput(regReq)){ //check that nothing other than OPTIONAL IDs is null
            return new RegisterResponse("Error: Invalid Input");
        }
        //make user and person from request
        makeUandP(regReq);

        try {
            Connection conn = myDB.getConnection();
            UserDAO myUDao = new UserDAO(conn);
            AuthTokenDAO myADao = new AuthTokenDAO(conn);

            User userTest = myUDao.find(registerUser.getUsername());

            if (userTest == null) { //if user not found, register
                authToken = new AuthToken(registerUser.getUsername());
                myADao.insert(authToken);
                myUDao.insert(registerUser);
                myDB.closeConnection(true);
                FillService fs = new FillService();
                FillResponse fr=new FillResponse();
                fr=fs.fill(registerUser.getUsername(), defaultNumOfGenerations); //call fill with 4 generations
                response.setAuthTok(authToken.getAuthTok());
                response.setUserName(registerUser.getUsername());
                response.setPersonId(registerPerson.getPersonID());
                response.setSuccessFlag(true);
            }
            else {
                myDB.closeConnection(true);
                response.setErrorMessage("Error: Username already exists");
                response.setSuccessFlag(false);
            }

        }catch (DataAccessException e){
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

    //checks to see if register input is valid, not null
    private boolean checkInput(RegisterReq regReq) {
        return !((regReq.getUsername() == null) || (regReq.getEmail() == null) || (regReq.getFirstName() == null) ||
                (regReq.getLastName() == null) || (regReq.getPassword() == null) || (regReq.getGender() == null));
    }

    private void makeUandP(RegisterReq regReq) {
        registerUser.setUsername(regReq.getUsername());
        registerUser.setPassword(regReq.getPassword());
        registerUser.setEmail(regReq.getEmail());
        registerUser.setFirstName(regReq.getFirstName());
        registerUser.setLastName(regReq.getLastName());
        registerUser.setGender(regReq.getGender());
        registerUser.setPersonID(registerPerson.getPersonID());

        registerPerson.setAssociatedUsername(registerUser.getUsername());
        registerPerson.setFirstName(registerUser.getFirstName());
        registerPerson.setLastName(registerUser.getLastName());
        registerPerson.setGender(registerUser.getGender());
    }
}
