package Model;

import java.util.UUID;

public class AuthToken {
    /**
     * unique authorization token string for the user
     */
    private String authtoken;
    /**
     * user’s username
     */
    private String username;

    public AuthToken(String name)
    {
        authtoken = UUID.randomUUID().toString();
        username = name;
    }
    public AuthToken(String username, String token){
        this.username = username;
        this.authtoken = token;
    }
    /**
     * default empty constructor
     */
    public AuthToken(){}
    public String getAuthTok() {
        return authtoken;
    }

    public void setAuthTok(String authTok) {
        this.authtoken = authTok;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        AuthToken a = (AuthToken) o;
        return authtoken.equals(a.getAuthTok()) && username.equals(a.getUsername());
    }
}
