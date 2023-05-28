package Handlers;

import Response.FillResponse;
import Services.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //Headers reqHeaders = exchange.getRequestHeaders();
                FillService fillService = new FillService();

                String uRI = exchange.getRequestURI().toString();
                String respData = "Invalid request";
                FillResponse fillRes = null;
                uRI = uRI.substring(6);
                if (uRI.contains("/")) {
                    int index = uRI.indexOf("/");
                    fillRes = fillService.fill(uRI.substring(0, index), Integer.parseInt(uRI.substring(index + 1)));
                    respData = gson.toJson(fillRes);

                }
                else {
                    int defaultNumOfGenerations = 4;
                    fillRes = fillService.fill(uRI, defaultNumOfGenerations);
                    respData = gson.toJson(fillRes);

                }

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
        }
        catch (IOException inputException) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            inputException.printStackTrace();
        }
    }
    private void writeString(String str, OutputStream outStream) throws IOException
    {
        OutputStreamWriter streamWriter = new OutputStreamWriter(outStream);
        streamWriter.write(str);
        streamWriter.flush();
    }

}
