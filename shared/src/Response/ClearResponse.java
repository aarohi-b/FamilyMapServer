package Response;

public class ClearResponse {
    /**
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error, otherwise .Clear succeeded.
     */
    private String message;
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
            this.message = "Clear succeeded.";
        }
        else
            this.message = errorMessage;
    }
}
