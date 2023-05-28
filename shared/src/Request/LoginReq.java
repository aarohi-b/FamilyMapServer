package Request;

public class LoginReq {
    /**
     * Non-empty string username
     */
    private String username;
    /**
     * Non-empty string password
     */
    private String password;

    public LoginReq(String username, String pass) {
        this.username=username;
        this.password=pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
