package Services;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Request.EventReq;
import Response.EventResponse;
import Response.PersonIDResponse;

import java.sql.Connection;

public class EventService {
    Database myDB = new Database();

    private Event[] events;
    /**
     * Will query database, looking for all family members of the currents user
     * @param request the request for this arrray of events
     * @return the correct EventReponse
     */
    public EventResponse retrieveEvents(EventReq request){
        EventResponse response = new EventResponse();

        String username = new String();
        try {
            Connection conn = myDB.getConnection();
            AuthTokenDAO authTokenDao = new AuthTokenDAO(conn);
            EventDAO eventDAO = new EventDAO(conn);
            AuthToken auth = authTokenDao.find(request.getAuthToken());

            if (auth == null) {
                response.setMessage("Error: Invalid AuthToken");
                response.setSuccessFlag(false);
            }
            else {
                //retrieve username associated with this authToken
                username = auth.getUsername();
                //if all checks pass, retrieve Event[] object.
                events = eventDAO.selectAllEvents(username);
                response.setSuccessFlag(true);
                response.setData(events);
            }
            myDB.closeConnection(true);
        } catch (DataAccessException e) {
            response.setSuccessFlag(false);
            response.setMessage(e.getMessage());
            try {
                myDB.closeConnection(false);
            } catch (DataAccessException d) {
                response.setSuccessFlag(false);
                response.setMessage(d.getMessage());
            }
        }
        return response;
    }
}