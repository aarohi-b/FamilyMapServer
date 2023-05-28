package DataAccess;

import Model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class interacts with the Event database Table (SQL)
 */
public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public Event[] selectAllEvents(String userName) throws DataAccessException{
        ArrayList<Event> outArray = new ArrayList<Event>();
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM Event WHERE associatedUsername = '" + userName + "'";
                stmt = conn.prepareStatement(sql);

                rs = stmt.executeQuery();
                while (rs.next()) {
                    Event out = new Event();
                    out.setEventID(rs.getString(1));
                    out.setAssociatedUsername(rs.getString(2));
                    out.setPersonID(rs.getString(3));
                    out.setLatitude(rs.getFloat(4));
                    out.setLongitude(rs.getFloat(5));
                    out.setCountry(rs.getString(6));
                    out.setCity(rs.getString(7));
                    out.setEventType(rs.getString(8));
                    out.setYear(rs.getInt(9));
                    outArray.add(out);
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Select all events failed");
        }
        if(outArray.size()==0){
            return null;
        }
        Event[] outFinal = new Event[outArray.size()];
        outFinal = outArray.toArray(outFinal);
        return outFinal;
    }
    public Event selectSingleEvent(String eventId) throws DataAccessException {
        Event out = new Event();
        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM Event WHERE eventID = '" + eventId +"'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    out.setEventID(rs.getString(1));
                    out.setAssociatedUsername(rs.getString(2));
                    out.setPersonID(rs.getString(3));
                    out.setLatitude(rs.getFloat(4));
                    out.setLongitude(rs.getFloat(5));
                    out.setCountry(rs.getString(6));
                    out.setCity(rs.getString(7));
                    out.setEventType(rs.getString(8));
                    out.setYear(rs.getInt(9));
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Single Event retrieval failed");
        }
        return out;
    }
    public void deleteEventFromUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Event WHERE associatedUsername = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,userName);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
    }
}
