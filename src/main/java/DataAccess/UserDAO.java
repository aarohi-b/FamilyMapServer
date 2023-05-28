package DataAccess;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insert (User toAdd) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, gender, personID) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want to fill in and
            //give it a proper value. The first argument corresponds to the first question mark found in our sql String
            stmt.setString(1, toAdd.getUsername());
            stmt.setString(2, toAdd.getPassword());
            stmt.setString(3, toAdd.getEmail());
            stmt.setString(4, toAdd.getFirstName());
            stmt.setString(5, toAdd.getLastName());
            stmt.setString(6, toAdd.getGender());
            //incorrect gender input
            if(!toAdd.getGender().equals("m") && !toAdd.getGender().equals("f"))
                throw new DataAccessException("Incorrect format, gender has to be “f” or “m”");
            //person id is not unique ie person already exists
            stmt.setString(7, toAdd.getPersonID());
            if (findID(toAdd.getPersonID())) {
                throw new DataAccessException("User exists");
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting person");
        }
    }
    public boolean findID(String personID) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE personID = '" + personID + "'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return false;
    }
    public User find(String username) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("personID"));
            }
            else{
                user=null;
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
        return user;
    }

    public void clear() throws DataAccessException { //clears tables
        ResultSet rs = null;
        String sql = "DELETE FROM User";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("clear User Table failed"); }
    }
}
