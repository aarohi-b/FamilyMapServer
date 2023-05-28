package Response;

public class FillResponse {
    /**
     * determine if response is a success
     */
    private boolean success;



    /**
     * If request fails, this contains a message with description of error, otherwise .Successfully added X persons and Y events to the database.
     */
    private String message;
    public FillResponse(boolean b, String msg) {
        this.message=msg;
        this.success=b;
    }

    public FillResponse() {}

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
            this.message = "Successfully added X persons and Y events to the database.";
        }
        else
            this.message = errorMessage;
    }
}
