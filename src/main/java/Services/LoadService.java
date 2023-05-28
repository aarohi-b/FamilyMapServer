package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadReq;
import Response.FillResponse;
import Response.LoadResponse;

import java.sql.Connection;
import java.util.UUID;

/**
 *A Load Service object which generates a LoadResponse object
 */
public class LoadService {
    Database myDB = new Database();

    public LoadResponse load(LoadReq loadReq) {
        LoadResponse response = new LoadResponse();
        try {
            ClearService clearService=new ClearService();
            clearService.clearDb();
            Connection conn = myDB.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            PersonDAO personDao = new PersonDAO(conn);
            UserDAO userDao = new UserDAO(conn);
            EventDAO eventDao = new EventDAO(conn);
            AuthToken authToken;

            //make sure all user, person and events are valid
            if (!(checkUsers(loadReq.getUsers()) && checkPersons(loadReq.getPersons()) && checkEvents(loadReq.getEvents()))){
                response.setErrorMessage(false, "Invalid input parameters");
                myDB.closeConnection(false);
                return response;
            }

            String insertUsersResp;
            if (loadReq.getUsers().length == 0){
                insertUsersResp="No Users to Insert";
            }
            else { //insert user
                for (int i = 0; i < loadReq.getUsers().length; i++) {
                    User userTest = userDao.find(loadReq.getUsers()[i].getUsername());
                    if (userTest == null) {
                        userDao.insert(loadReq.getUsers()[i]);
                        authToken = new AuthToken(loadReq.getUsers()[i].getUsername());
                        authTokenDao.insert(authToken);
                    } else {
                        insertUsersResp= "User already exists!";
                    }
                }
                insertUsersResp="Done inserting users";
            }

            String insertPersonsResp;
            if (loadReq.getPersons().length == 0){
                insertPersonsResp="No Persons to Insert";
            }
            else { //insert person
                for (int i = 0; i < loadReq.getPersons().length; i++) {
                    personDao.insert(loadReq.getPersons()[i]);
                }
                insertPersonsResp="Done inserting persons";
            }

            String insertEventResp;
            if (loadReq.getEvents().length == 0){
                insertEventResp="No Events to Insert";
            }
            else { //insert event
                for (int i = 0; i < loadReq.getEvents().length; i++) {
                    eventDao.insert(loadReq.getEvents()[i]);
                }
                insertEventResp="Done inserting events";
            }

            if(!insertUsersResp.equals("Done inserting users")){
                response.setErrorMessage(true, insertUsersResp);
            }
            else if (!insertPersonsResp.equals("Done inserting persons")){
                response.setErrorMessage(true, insertPersonsResp);
            }
            else if (!insertEventResp.equals("Done inserting events")){
                response.setErrorMessage(true, insertEventResp);
            }
            else {
                myDB.closeConnection(true);
                return new LoadResponse(true, "Successfully added " + loadReq.getUsers().length + " users, " + loadReq.getPersons().length
                        + " persons, and " + loadReq.getEvents().length + " events to the database");
            }
            myDB.closeConnection(true);
        }
        catch (DataAccessException e){
            response.setSuccessFlag(false);
            response.setErrorMessage(false, e.getMessage());
            try{
                myDB.closeConnection(false);
            }catch (DataAccessException d){
                response.setSuccessFlag(false);
                response.setErrorMessage(false, d.getMessage());
            }
        }
        return response;
    }

    //validate user
    private boolean checkUsers(User[] userArray) {
        for (int i = 0; i < userArray.length; i++){
            User currUser = userArray[i];
            if (currUser.getUsername() == null || currUser.getPassword() == null || currUser.getFirstName() == null ||
                    currUser.getLastName() == null || currUser.getEmail() == null || currUser.getGender() == null ||
                    currUser.getPersonID() == null) {
                return false;
            }
        }
        return true;
    }

    //validate person
    private boolean checkPersons(Person[] personArray) {
        for (int j = 0; j < personArray.length; j++){
            Person currPerson = personArray[j];
            if (currPerson.getPersonID() == null || currPerson.getAssociatedUsername() == null ||
                    currPerson.getFirstName() == null || currPerson.getLastName() == null || currPerson.getGender() == null) {
                return false;
            }
        }
        return true;
    }

    //validate event
    private boolean checkEvents(Event[] eventArray) {
        for (int k = 0; k < eventArray.length; k++){
            Event currEvent = eventArray[k];
            if (currEvent.getEventID() == null || currEvent.getAssociatedUsername() == null || currEvent.getPersonID() == null ||
                    currEvent.getCity() == null || currEvent.getCountry() == null || currEvent.getEventType() == null) {
                return false;
            }
        }
        return true;
    }
}
