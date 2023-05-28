package Response;

import Model.Event;

public class EventResponse {
    /**
     * Array of Event objects
     */
    Event[] data;
    /**
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public boolean isSuccessFlag() {
        return success;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.success = successFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
