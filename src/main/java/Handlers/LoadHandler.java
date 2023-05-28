package Handlers;

import Request.LoadReq;
import Response.LoadResponse;
import Services.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        LoadResponse myLoadResponse = new LoadResponse();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoadService myLoadService = new LoadService();
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                LoadReq loadReq = gson.fromJson(reader, LoadReq.class);
                myLoadResponse = myLoadService.load(loadReq);

                String respData = gson.toJson(myLoadResponse);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);
                respBody.close();
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
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