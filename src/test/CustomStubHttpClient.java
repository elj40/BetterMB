import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import javax.net.ssl.SSLSession;
import java.net.URI;
import java.util.Optional;
import java.io.IOException;

class CustomStubHttpClient implements IHttpClient
{
    HttpResponse<String> response;
    boolean shouldThrowException = false;
    IOException exception;
    @Override
    public HttpResponse<String> send(HttpRequest request, BodyHandler bodyHandler) throws IOException
    {
        if (shouldThrowException) throw exception;
        return response;
    };
    public void setThrowException(IOException e)
    {
        shouldThrowException = true;
        exception = e;
    }
    public void setResponseFromString(int statusCode, String msg)
    {
        response = new HttpResponse<String>() {
            @Override
            public int statusCode() { return statusCode; }
            @Override
            public String body() { return msg; };
            @Override
            public HttpRequest request() { return null; };
            @Override
            public HttpHeaders headers() { return null; };
            @Override
            public URI uri() { return null; };
            @Override
            public Optional<HttpResponse<String>> previousResponse() { return null; };
            @Override
            public HttpClient.Version version() { return null; };
            @Override
            public Optional<SSLSession> sslSession() { return null; };
        };
    };
};
