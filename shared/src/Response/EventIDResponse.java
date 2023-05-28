package Response;

import Model.Event;

public class EventIDResponse {
    /**
     * Username of user account this event belongs to
     */
    private String associatedUsername;
    /**
     * Event’s unique ID (non-empty string)
     */
    private String eventID;
    /**
     * ID of the person this event belongs to (non-empty string)
     */
    private String personID;
    /**
     * Latitude of the event’s location (number)
     */
    private float latitude;
    /**
     * Longitude of the event’s location (number)
     */
    private float longitude;
    /**
     * Name of country where event occurred
     */
    private String country;
    /**
     * Name of city where event occurred (non-empty string)
     */
    private String city;
    /**
     * Type of event (birth, baptism, etc.) (non-empty string)
     */
    private String eventType;
    /**
     * Year the event occurred (integer)
     */
    private int year;
    /**
     * determine if response is a success
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public EventIDResponse(Event events, String userName)
    {
        eventID = events.getEventID();
        associatedUsername = userName;
        personID = events.getPersonID();
        latitude = events.getLatitude();
        longitude = events.getLongitude();
        country = events.getCountry();
        city = events.getCity();
        eventType = events.getEventType();
        year = events.getYear();
    }

    public EventIDResponse() {

    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
