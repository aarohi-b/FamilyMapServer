package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler implements HttpHandler{
    public final static String FILE_ROOT_DIRECTORY= "web";
    /**
     * The default handler
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPath = exchange.getRequestURI().getPath();
                if (urlPath.length() == 0 || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String filePath = FILE_ROOT_DIRECTORY + urlPath;
                File file = new File(filePath);

                //if correct url, success
                if(file.exists() && file.canRead()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream responseBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), responseBody);
                    responseBody.close();
                    success = true;
                }

                //if not correct url, direct to 404 error webpage
                else{
                    File error404 = new File("web/HTML/404.html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream responseBody = exchange.getResponseBody();
                    Files.copy(error404.toPath(), responseBody);
                    responseBody.close();
                }
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}