package Response;

public class LoadResponse {
    /**
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error, otherwise .Successfully added X users, Y persons, and Z events to the database.
     */
    private String message;

    public LoadResponse(boolean b , String s) {
        this.message=s;
        this.success=b;
    }


    public LoadResponse() { }

    public boolean getSuccessFlag() {
        return success;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.success = successFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setErrorMessage(boolean successFlag, String errorMessage) {
        if(successFlag){
            this.message = "Successfully added X users, Y persons, and Z events to the database.";
        }
        else
            this.message = errorMessage;
    }
}
