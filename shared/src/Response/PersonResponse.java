package Response;

import Model.Event;
import Model.Person;

import java.util.ArrayList;

public class PersonResponse {
    /**
     * Array of Person objects
     */
    private ArrayList<Person> data;
    /**
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public PersonResponse(String s) {
        this.message=s;
    }

    public PersonResponse(ArrayList<Person> allPeople) {
        this.data=allPeople;
    }

    public PersonResponse() {

    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList data) {
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
