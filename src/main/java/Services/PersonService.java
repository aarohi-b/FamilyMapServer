package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Response.EventIDResponse;
import Response.PersonIDResponse;
import Response.PersonResponse;

import java.sql.Connection;
import java.util.ArrayList;

public class PersonService {
    Database myDB = new Database();

    //find a single person, calls find(single) from PERSONDAO
    public PersonIDResponse singlePerson(String personID, String authToken) {
        PersonIDResponse myResponse = new PersonIDResponse();
        try {
            Connection conn = myDB.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            PersonDAO personDao = new PersonDAO(conn);
            AuthToken checkedAuthToken = authTokenDao.find(authToken);

            if (checkedAuthToken == null) {
                myResponse.setErrorMessage("Error: Invalid Authorization Token");
            } else {
                Person foundPerson = personDao.find(personID);
                if (foundPerson == null) {
                    myResponse.setErrorMessage("Error: Person not found");
                } else if (!checkedAuthToken.getUsername().equals(foundPerson.getAssociatedUsername())) {
                    myResponse.setErrorMessage("Error: Person not registered under current user");
                } else {
                    PersonIDResponse result = new PersonIDResponse(foundPerson);
                    myDB.closeConnection(true);
                    result.setSuccessFlag(true);
                    return result;
                }
            }
            myDB.closeConnection(true);
            myResponse.setSuccessFlag(false);

        } catch (DataAccessException e) {
            myResponse.setSuccessFlag(false);
            myResponse.setErrorMessage(e.getMessage());
            try {
                myDB.closeConnection(false);
            } catch (DataAccessException d) {
                myResponse.setSuccessFlag(false);
                myResponse.setErrorMessage(d.getMessage());
            }
        }
        return myResponse;
    }

    //find all people with given authtoken, calls find all from PERSONDAO
    public PersonResponse everyPerson(String authToken) {
        PersonResponse myResponse = new PersonResponse();
        try {
            Connection conn = myDB.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            PersonDAO personDao = new PersonDAO(conn);
            AuthToken checkedAuthToken = authTokenDao.find(authToken);

            if (checkedAuthToken == null){
                myResponse.setMessage("Error: There are no people under user");
            }
            else {
                ArrayList allPeople = personDao.findAll(checkedAuthToken.getUsername());

                if (allPeople == null){
                    myResponse.setMessage("Error: There are no people under user");
                }
                else {
                    PersonResponse result = new PersonResponse(allPeople);
                    result.setSuccessFlag(true);
                    myDB.closeConnection(true);
                    return result;
                }
            }
            myDB.closeConnection(true);
        }
        catch (DataAccessException e) {
            myResponse.setSuccessFlag(false);
            myResponse.setMessage(e.getMessage());
            try {
                myDB.closeConnection(false);
            } catch (DataAccessException d) {
                myResponse.setSuccessFlag(false);
                myResponse.setMessage(d.getMessage());
            }
        }
        return myResponse;
    }
}
