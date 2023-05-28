package DataAccess;

import Model.AuthToken;
import Model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;
    public AuthTokenDAO(Connection conn) { this.conn = conn; }

    public void insert(AuthToken auth) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (username, token) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, auth.getUsername());
            stmt.setString(2, auth.getAuthTok());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting authtoken into the database");
        }
    }

    public AuthToken find(String authToken) throws DataAccessException {
        AuthToken auth;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE token  = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                auth = new AuthToken(rs.getString("username"), rs.getString("token"));
                return auth;
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
}
