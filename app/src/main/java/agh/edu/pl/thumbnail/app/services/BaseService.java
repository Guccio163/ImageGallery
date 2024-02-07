package agh.edu.pl.thumbnail.app.services;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BaseService {
    protected HttpClient httpClient;
    protected String baseUrl;
    public BaseService(String url)
    {
        baseUrl = url;
        httpClient = HttpClient.newHttpClient();
    }

    protected static <T> boolean isSuccessfullStatusCode(HttpResponse<T> response){
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }
}
