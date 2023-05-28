package Services;
import Model.Event;
import com.google.gson.*;
import java.io.*;
import java.util.Random;

public class Names {
    public String generateMaleName()
    {
        Random rand = new Random();

        try {
            FileReader fileReader = new FileReader(new File("json/mnames.json"));
            JsonParser jsonParser = new JsonParser();
            JsonObject rootObject = (JsonObject) jsonParser.parse(fileReader);
            JsonArray nameArray = (JsonArray) rootObject.get("data");

            int index = rand.nextInt(nameArray.size());
            String nameM = nameArray.get(index).toString();
            nameM = nameM.substring(1, nameM.length() - 1);
            return nameM;
        }
        catch (FileNotFoundException fileNotFound){
            fileNotFound.printStackTrace();
        }

        return "Error in JSON";
    }

    public String generateFemaleName()
    {
        Random rand = new Random();

        try {
            FileReader fileReader = new FileReader(new File("json/fnames.json"));
            JsonParser jsonParser = new JsonParser();
            JsonObject rootObject = (JsonObject) jsonParser.parse(fileReader);
            JsonArray nameArray = (JsonArray) rootObject.get("data");

            int index = rand.nextInt(nameArray.size());
            String nameF = nameArray.get(index).toString();
            nameF = nameF.substring(1, nameF.length() - 1);
            return nameF;
        }
        catch (FileNotFoundException fileNotFound){
            fileNotFound.printStackTrace();
        }

        return "Error in JSON";
    }
    public String generateLastName()
    {
        Random rand = new Random();

        try {
            FileReader fileReader = new FileReader(new File("json/snames.json"));
            JsonParser jsonParser = new JsonParser();
            JsonObject rootObject = (JsonObject) jsonParser.parse(fileReader);
            JsonArray nameArray = (JsonArray) rootObject.get("data");

            int index = rand.nextInt(nameArray.size());
            String nameLast = nameArray.get(index).toString();
            nameLast = nameLast.substring(1, nameLast.length() - 1);
            return nameLast;
        }
        catch (FileNotFoundException fileNotFound){
            fileNotFound.printStackTrace();
        }

        return "Error in JSON";
    }
    public Event generateLoc() {
        Random rand = new Random();
        Event eventWithLocation = new Event();

        try {
            FileReader fileReader = new FileReader(new File("json/locations.json"));
            JsonParser jsonParser = new JsonParser();
            JsonObject rootObject = (JsonObject) jsonParser.parse(fileReader);
            JsonArray locArray = (JsonArray) rootObject.get("data");

            int index = rand.nextInt(locArray.size());
            JsonObject currLocation = (JsonObject)locArray.get(index);

            String country = currLocation.get("country").toString()
                    .substring(1, currLocation.get("country").toString().length() - 1);
            String city = currLocation.get("city")
                    .toString().substring(1, currLocation.get("city").toString().length() - 1);

            eventWithLocation.setCity(city);
            eventWithLocation.setCountry(country);
            eventWithLocation.setLatitude(currLocation.get("latitude").getAsFloat());
            eventWithLocation.setLongitude(currLocation.get("longitude").getAsFloat());

            return eventWithLocation;
        }
        catch (FileNotFoundException fileNotFound){
            fileNotFound.printStackTrace();
        }

        return new Event();
    }
}
