package Services;
import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Response.FillResponse;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * A Fill Service object which generates a FillResponse object
 */
public class FillService {
    private Random random = new Random();
    private Names nameGenerator = new Names();
    private ArrayList<Person> familyTree=new ArrayList<>();
    private ArrayList<Event> allEvents = new ArrayList<>();
    private Database myDB;

    public FillService(){
        myDB = new Database();
    }
    public FillResponse fill(String username, int generation){
        FillResponse response = new FillResponse();
        if (generation <= 0) {
            return new FillResponse(false,"Error: Number of Generations invalid");
        }
        try {
            Connection conn = myDB.getConnection();
            EventDAO myEDao = new EventDAO(conn);
            UserDAO myUDao = new UserDAO(conn);
            PersonDAO myPDao = new PersonDAO(conn);
            User user = myUDao.find(username);
            if (user==null) {
                throw new DataAccessException("Error: username does not exist");
            }
            else {
                //delete person if they already exist, so we have a fresh start
                myPDao.deletePersonFromUser(user.getUsername());
                myEDao.deleteEventFromUser(user.getUsername());

                Person userPerson = new Person(user);
                userPerson.setMotherID(UUID.randomUUID().toString());
                userPerson.setFatherID(UUID.randomUUID().toString());
                userPerson.setSpouseID(UUID.randomUUID().toString());
                familyTree.add(userPerson);

                int currYear = 2000;
                createBirth(userPerson, currYear);
                generateParents(userPerson, generation, currYear);
                for(int i=0; i<allEvents.size();i++){
                    myEDao.insert(allEvents.get(i));
                }
                for(int i=0; i<familyTree.size();i++){
                    myPDao.insert(familyTree.get(i));
                }

                myDB.closeConnection(true);
                return new FillResponse(true, "Successfully added " + familyTree.size() + " persons and "
                        + allEvents.size() + " events to the database.");
            }

        }catch (DataAccessException e){
            response.setSuccessFlag(false);
            response.setErrorMessage(false, e.getMessage());
            try{
                myDB.closeConnection(false);
            }catch (DataAccessException d){
                response.setSuccessFlag(false);
                response.setErrorMessage(false, d.getMessage());
            }
        }
        return response;
    }

    public void createBirth(Person currPerson, int currYear){
        Event birth = nameGenerator.generateLoc();
        birth.setEventID(UUID.randomUUID().toString());
        int yearOfBirth = 0;
        yearOfBirth = currYear - random.nextInt(10);

        birth.setPersonID(currPerson.getPersonID());
        birth.setEventType("Birth");
        birth.setAssociatedUsername(currPerson.getAssociatedUsername());
        birth.setYear(yearOfBirth);
        allEvents.add(birth);
    }

    public void createDeath(Person currPerson, int currYear) {
        Event death = nameGenerator.generateLoc();
        death.setEventID(UUID.randomUUID().toString());
        int averageLifespan = 30;

        int yearOfDeath = currYear + averageLifespan + random.nextInt(50);

        if (yearOfDeath > 2021){
            yearOfDeath = 2021;
        }

        death.setPersonID(currPerson.getPersonID());
        death.setEventType("Death");
        death.setAssociatedUsername(currPerson.getAssociatedUsername());
        death.setYear(yearOfDeath);

        allEvents.add(death);
    }

    public void createMarriage(Person husband, Person wife, int currYear) {
        int yearOfMarriage = 0;
        yearOfMarriage = currYear + random.nextInt(6) + 22;

        Event currMarriage = nameGenerator.generateLoc();
        currMarriage.setEventID(UUID.randomUUID().toString());
        currMarriage.setAssociatedUsername(husband.getAssociatedUsername());
        currMarriage.setEventType("Marriage");
        currMarriage.setYear(yearOfMarriage);
        currMarriage.setPersonID(husband.getPersonID());

        Event marriageDup = new Event();
        marriageDup.setEventID(UUID.randomUUID().toString());
        marriageDup.setPersonID(wife.getPersonID());
        marriageDup.setEventType("Marriage");
        marriageDup.setAssociatedUsername(wife.getAssociatedUsername());
        marriageDup.setYear(yearOfMarriage);
        marriageDup.setLongitude(currMarriage.getLongitude());
        marriageDup.setLatitude(currMarriage.getLatitude());
        marriageDup.setCountry(currMarriage.getCountry());
        marriageDup.setCity(currMarriage.getCity());

        allEvents.add(currMarriage);
        allEvents.add(marriageDup);

    }
    private void generateParents(Person currPerson, int generation, int currYear) {
        int generationalGap = 30;
        currYear = currYear - generationalGap - random.nextInt(10);
        Person mother = new Person(currPerson.getMotherID());
        Person father = new Person(currPerson.getFatherID());

        father.setFatherID(UUID.randomUUID().toString());
        father.setMotherID(UUID.randomUUID().toString());

        father.setFirstName(nameGenerator.generateMaleName());
        father.setGender("m");
        father.setSpouseID(mother.getPersonID());
        father.setAssociatedUsername(currPerson.getAssociatedUsername());
        if (currPerson.getGender().equals("m")) {
            father.setLastName(currPerson.getLastName());
        }
        else {
            father.setLastName(nameGenerator.generateLastName());
        }
        familyTree.add(father);

        mother.setFirstName(nameGenerator.generateFemaleName());
        mother.setLastName(nameGenerator.generateLastName());
        mother.setSpouseID(father.getPersonID());
        mother.setGender("f");

        mother.setFatherID(UUID.randomUUID().toString());
        mother.setMotherID(UUID.randomUUID().toString());

        mother.setAssociatedUsername(currPerson.getAssociatedUsername());
        familyTree.add(mother);

        //create mother father events
        createBirth(father, currYear);
        createBirth(mother, currYear);
        createMarriage(father, mother, currYear);
        createDeath(father, currYear);
        createDeath(mother, currYear);

        //stop creating generations
        if(generation==1) {
            father.setFatherID(null);
            father.setMotherID(null);
            mother.setMotherID(null);
            mother.setFatherID(null);
        }
        generation--;

        //recursion, only if there are more generations to create
        if (generation > 0) {
            generateParents(mother, generation, currYear);
            generateParents(father, generation, currYear);
        }
    }
}