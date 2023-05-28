package Response;

public class LoginResponse {
    /**
     * Non-empty auth token string
     */
    private String authtoken;
    /**
     * Username passed in with request
     */
    private String username;
    /**
     * Non-empty string containing the Person ID of the user’s generated Person object
     */
    private String personID;
    /**
     * If request fails, this flag is marked false.  If it succeeds, it is marked true.
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public LoginResponse(boolean b, String s) {
        this.message=s;
        this.success=b;
    }

    public LoginResponse() { }

    public LoginResponse(String authTok, String username, String personID) {
            this.authtoken = authTok;
            this.username = username;
            this.personID = personID;
            this.message = null;
            this.success=true;
    }

    public String getAuthTok() {
        return authtoken;
    }

    public String getUserName() {
        return username;
    }

    public String getPersonId() {
        return personID;
    }

    public void setAuthTok(String authTok) {
        this.authtoken = authTok;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public void setPersonId(String personId) {
        this.personID = personId;
    }

    public boolean getSuccessFlag() {
        return success;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.success = successFlag;
    }

    public String getErrorMessage() {
        return message;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }
}
