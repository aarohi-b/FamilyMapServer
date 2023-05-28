package Handlers;

import Request.EventIDRequest;
import Request.EventReq;
import Response.EventIDResponse;
import Response.EventResponse;
import Services.EventIDService;
import Services.EventService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            boolean success = false;

            try {
                if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                    String urlPath = exchange.getRequestURI().getPath();
                    String strArray[] = urlPath.split("/");
                    //Handle EVENT
                    if (strArray.length == 2) {
                        System.out.println("Handle EVENT");
                        EventService eventService = new EventService();
                        EventReq eventRequest = new EventReq();
                        EventResponse eventResponse = new EventResponse();
                        Gson gson = new Gson();
                        Headers reqHeaders = exchange.getRequestHeaders();
                        if (reqHeaders.containsKey("Authorization")) {
                            String authToken = reqHeaders.getFirst("Authorization");
                            eventRequest.setAuthToken(authToken);
                            eventResponse = eventService.retrieveEvents(eventRequest);
                            String str = gson.toJson(eventResponse);
                            byte[] array = str.getBytes();
                            success=eventResponse.isSuccessFlag();
                            if(success) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            }
                            else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            }
                            OutputStream resBody = exchange.getResponseBody();
                            resBody.write(array);
                            resBody.close();
                        }
                    }

                    //Handle single EVENT ID
                    else if (strArray.length == 3) {
                        System.out.println("Handle EventID");
                        EventIDService eventIdService = new EventIDService();
                        EventIDRequest eventIdRequest = new EventIDRequest();
                        EventIDResponse eventIdResponse = new EventIDResponse();
                        Gson gson = new Gson();
                        Headers reqHeaders = exchange.getRequestHeaders();
                        if (reqHeaders.containsKey("Authorization")) {
                            String authToken = reqHeaders.getFirst("Authorization");
                            eventIdRequest.setEventID(strArray[2]);
                            eventIdResponse = eventIdService.retrieveEvent(eventIdRequest.getEventID(), authToken);
                            String str = gson.toJson(eventIdResponse);
                            byte[] array = str.getBytes();
                            success=eventIdResponse.isSuccessFlag();
                            if(success) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            }
                            else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            }
                            OutputStream resBody = exchange.getResponseBody();
                            resBody.write(array);
                            resBody.close();
                        }
                    }
                }
            } catch (IOException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                exchange.getResponseBody().close();
                e.printStackTrace();
            }
        }
}
