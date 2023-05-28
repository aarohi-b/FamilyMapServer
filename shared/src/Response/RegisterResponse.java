package Response;

public class RegisterResponse {
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
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public RegisterResponse(String s) {
        this.message=s;
    }

    public RegisterResponse(String authTok, String username, String personID) {
        this.authtoken=authTok;
        this.username=username;
        this.personID=personID;
        this.message = null;
    }

    public RegisterResponse() { }

    public String getAuthTok() {
        return authtoken;
    }

    public void setAuthTok(String authTok) {
        this.authtoken = authTok;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPersonId() {
        return personID;
    }

    public void setPersonId(String personId) {
        this.personID = personId;
    }

    public boolean isSuccessFlag() {
        return success;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.success = successFlag;
    }

    public String getErrorMessage() {
        return message;
    }

    public void setErrorMessage(String errorMessage) {
        this.message = errorMessage;
    }
}
