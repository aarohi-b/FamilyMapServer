package Response;

import Model.Person;

public class PersonIDResponse {
    /**
     * Username of user account this person belongs to
     */
    private String associatedUsername;
    /**
     * Person’s unique ID (non-empty string)
     */
    private String personID;
    /**
     * Person’s first name
     */
    private String firstName;
    /**
     * Person’s last name
     */
    private String lastName;
    /**
     * Person’s gender (m or f)
     */
    private String gender;
    /**
     * ID of person’s father [OPTIONAL, can be missing]
     */
    private String fatherID;
    /**
     * ID of person’s mother [OPTIONAL, can be missing]
     */
    private String motherID;
    /**
     * ID of person’s spouse [OPTIONAL, can be missing]
     */
    private String spouseID;
    /**
     * If request fails, this flag is marked false.  If it succeeds, it is marked true.
     */
    private boolean success;
    /**
     * If request fails, this contains a message with description of error
     */
    private String message;

    public PersonIDResponse(String s) {
        this.message=s;
    }
    public PersonIDResponse(Person singlePerson) {
        personID = singlePerson.getPersonID();
        associatedUsername = singlePerson.getAssociatedUsername();
        firstName = singlePerson.getFirstName();
        lastName = singlePerson.getLastName();
        gender = singlePerson.getGender();
        fatherID = singlePerson.getFatherID();
        motherID = singlePerson.getMotherID();
        spouseID = singlePerson.getSpouseID();
    }

    public PersonIDResponse() {

    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
