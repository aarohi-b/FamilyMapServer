package Request;

public class EventReq {
    private String authtoken;

    public EventReq(String s) {
        this.authtoken = s;
    }

    public EventReq() {

    }

    public String getAuthToken() {
        return authtoken;
    }

    public void setAuthToken(String authToken) {
        this.authtoken = authToken;
    }
}
