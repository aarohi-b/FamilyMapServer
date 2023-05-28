package Request;

import Model.Event;
import Model.Person;
import Model.User;

public class LoadReq {
    /**
     * Array of User objects
     */
    User[] users;
    /**
     * Array of Person objects
     */
    Person[] persons;
    /**
     * Array of Event objects
     */
    Event[] events;

    public LoadReq(User[] userArray, Person[] personArray, Event[] eventArray) {
        this.users=userArray;
        this.persons=personArray;
        this.events=eventArray;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
