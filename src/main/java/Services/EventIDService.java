package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Response.EventIDResponse;

import java.sql.Connection;

/**
 *A EventID Service object which generates an EventIDResponse object
 */
public class EventIDService {
    Database myDB = new Database();

    public EventIDResponse retrieveEvent(String eventId, String authToken) {
        EventIDResponse myResponse = new EventIDResponse();

        try{
            Connection conn = myDB.getConnection();
            EventDAO myEDao = new EventDAO(conn);
            AuthTokenDAO myADao = new AuthTokenDAO(conn);
            AuthToken auth = myADao.find(authToken);

            if(auth==null){
                myResponse.setMessage("Error: Auth token could not be found");
                myResponse.setSuccessFlag(false);
            }
            else{
                //find event in database, if found, select it
                Event event1 = myEDao.find(eventId);
                if(event1!=null){
                    Event event = myEDao.selectSingleEvent(eventId);
                    if (!event.getAssociatedUsername().equals(auth.getUsername())){
                        myDB.closeConnection(false);
                        myResponse.setSuccessFlag(false);
                        myResponse.setMessage("Error: Descendant of event and username of auth token do not match");
                        return myResponse;
                    }
                    EventIDResponse resultToReturn = new EventIDResponse(event1, auth.getUsername());
                    myDB.closeConnection(true);
                    resultToReturn.setSuccessFlag(true);
                    return resultToReturn;
                }
            }
            myDB.closeConnection(true);

        } catch (DataAccessException e){
            myResponse.setSuccessFlag(false);
            myResponse.setMessage(e.getMessage());
            try{
                myDB.closeConnection(false);
            }catch (DataAccessException e2){
                myResponse.setSuccessFlag(false);
                myResponse.setMessage(e2.getMessage());
            }
        }
        return myResponse;
    }
}
