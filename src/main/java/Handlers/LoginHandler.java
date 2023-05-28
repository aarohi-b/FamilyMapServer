package Handlers;

import Request.LoadReq;
import Request.LoginReq;
import Response.LoadResponse;
import Response.LoginResponse;
import Services.LoadService;
import Services.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        LoginResponse myLoginResponse = new LoginResponse();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoginService myLoadService = new LoginService();
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                LoginReq loginReq = gson.fromJson(reader, LoginReq.class);
                myLoginResponse = myLoadService.login(loginReq);

                String respData = gson.toJson(myLoginResponse);
                success = myLoginResponse.getSuccessFlag();
                if(success==false){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);
                respBody.close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
