package DataAccess;

import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO{
    private final Connection conn;
    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insert (Person toAdd ) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, fatherID, " +
                "motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, toAdd.getPersonID());
            if (find(toAdd.getPersonID())!=null) {
                throw new DataAccessException("User exists");
            }
            stmt.setString(2, toAdd.getAssociatedUsername());
            stmt.setString(3, toAdd.getFirstName());
            stmt.setString(4, toAdd.getLastName());
            stmt.setString(5, toAdd.getGender());
            //incorrect gender input
            if(!toAdd.getGender().equals("m") && !toAdd.getGender().equals("f"))
                throw new DataAccessException("Incorrect format, gender has to be “f” or “m”");
            stmt.setString(6, toAdd.getFatherID());
            stmt.setString(7, toAdd.getMotherID());
            stmt.setString(8, toAdd.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public Person find(String personID) throws DataAccessException{
        //returns person object if found, else returns null
        Person person=null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE personID = '" + personID + "'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        //return person;
    }

    public ArrayList<Person> findAll(String userName) throws DataAccessException { //person model is user's person representation
        ArrayList<Person> out = new ArrayList<Person>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            if (rs == null) {
                throw new DataAccessException("There are no people under user");
            }
            while (rs.next()) {
                Person tempPerson = new Person();
                tempPerson.setPersonID(rs.getString(1));
                tempPerson.setAssociatedUsername(rs.getString(2));
                tempPerson.setFirstName(rs.getString(3));
                tempPerson.setLastName(rs.getString(4));
                tempPerson.setGender(rs.getString(5));
                tempPerson.setFatherID(rs.getString(6));
                tempPerson.setMotherID(rs.getString(7));
                tempPerson.setSpouseID(rs.getString(8));
                out.add(tempPerson);
                tempPerson = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        if (out.size() == 0) {
            return null;
        } else {
            return out;
        }
    }

    public void clear() throws DataAccessException { //clears tables
        ResultSet rs = null;
        String sql = "DELETE FROM Person";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("clear Person Table failed"); }
    }
    public void deletePersonFromUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE associatedUsername = '" + userName + "'";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
    }
}
