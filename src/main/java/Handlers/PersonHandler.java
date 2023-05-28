package Handlers;

import Response.PersonIDResponse;
import Response.PersonResponse;
import Services.PersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    PersonService personService = new PersonService();

                    String authToken = reqHeaders.getFirst("Authorization");
                    String uRI = exchange.getRequestURI().toString();
                    String respData = "Invalid request";

                    //more than one person
                    if (uRI.equals("/person")){
                        PersonResponse allResult = personService.everyPerson(authToken);
                        success=allResult.isSuccessFlag();
                        respData = gson.toJson(allResult);
                    }

                    //specific person, represented by person ID
                    else if (uRI.substring(0,8).equals("/person/")){
                        PersonIDResponse singleResult = personService.singlePerson(uRI.substring(8), authToken);
                        success=singleResult.isSuccessFlag();
                        respData = gson.toJson(singleResult);
                    }
                    else {
                        respData = "Error: Invalid request";
                    }

                    if(success){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
                }
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    //writes a string to an outputStream
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
