package eu.miaplatform.customplugin.springboot.controllers.Decorators;

import eu.miaplatform.service.Protocol;
import okhttp3.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpRequest.request;

@RunWith(SpringRunner.class)
public class PostDecoratorTest {
    protected final static int HTTP_ENDPOINT_PORT = 8081;
    private ClientAndServer mockServer;

    @Before
    public void startServer() {
        mockServer = startClientAndServer(HTTP_ENDPOINT_PORT);
    }

    @After
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void notifySlackTest() throws Exception {
        final String responseBody = "{ foo: 'bar' }";
        final String mockedService = "/notify-slack";
        final String JSONRequest = "{"+
            "\"request\":{"+
                "\"method\":null,"+
                "\"path\":null,"+
                "\"headers\":null,"+
                "\"query\":null,"+
                "\"body\":{"+
                    "\"who\":\"foo\","+
                    "\"mymsg\":\"bar\""+
            "}},"+
            "\"response\":{"+
                "\"body\":{"+
                    "\"who\":\"foo\","+
                    "\"mymsg\":\"bar\""+
                "},"+
                "\"headers\":null,"+
                "\"statusCode\":200"+
            "}}";

        mockServer.when(
            request()
                    .withMethod("POST")
                    .withPath(mockedService)
                    .withHeader("\"Content-type\", \"application/json\""),
                exactly(1))
        .respond(
                response()
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withStatusCode(200)
                        .withBody(responseBody)
        );

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .build();
        HttpUrl url = new HttpUrl.Builder()
                .scheme(Protocol.HTTP.toString())
                .host("localhost")
                .port(HTTP_ENDPOINT_PORT)
                .addPathSegment("notify-slack")
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody reqBody = RequestBody.create(JSON, JSONRequest);
        Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(response.code(), 200);
        assertEquals(Objects.requireNonNull(response.body()).string(), responseBody);
    }
}
