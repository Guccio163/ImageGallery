package agh.edu.pl.thumbnail.app.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {
    private static HttpService instance;

    public static HttpService getInstance() {
        if (instance == null)
            instance = new HttpService();
        return instance;
    }

    private HttpService() {}

    public HttpResponse<String> sendPost(String url, String json) {
        try {
            return sendPost(new URI(url), json);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> sendPost(URI uri, String json) {
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public HttpResponse<String> sendGet(String url) {
        try {
            return sendGet(new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> sendGet(URI uri) {
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            return httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
