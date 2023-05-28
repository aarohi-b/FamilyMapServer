package Request;

public class RegisterReq {
    /**
     * Non-empty string username
     */
    private String username;
    /**
     * Non-empty string password
     */
    private String password;
    /**
     * Non-empty string email
     */
    private String email;
    /**
     * Non-empty string firstName
     */
    private String firstName;
    /**
     * Non-empty string lastName
     */
    private String lastName;
    /**
     * f or m
     */
    private String gender;

    public RegisterReq(String username, String password, String email, String first, String last, String gender) {
        this.username=username;
        this.password=password;
        this.email=email;
        this.firstName=first;
        this.lastName=last;
        this.gender=gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
