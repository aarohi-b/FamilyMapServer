package Handlers;

import java.io.*;
import java.net.*;

import Request.EventIDRequest;
import Request.EventReq;
import Request.LoginReq;
import Request.RegisterReq;
import Response.EventIDResponse;
import Response.EventResponse;
import Response.RegisterResponse;
import Services.EventIDService;
import Services.EventService;
import Services.LoginService;
import Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                RegisterService rs = new RegisterService();
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                RegisterReq rr = gson.fromJson(reader, RegisterReq.class);
                RegisterResponse response = rs.register(rr);

                String respData = gson.toJson(response);
                success= response.isSuccessFlag();
                if(success){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);
                respBody.close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}