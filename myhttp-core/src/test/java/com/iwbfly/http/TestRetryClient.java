package com.iwbfly.http;

import com.iwbfly.http.client.RetryClient;
import com.iwbfly.mock.MockServerRequest;
import com.iwbfly.myhttp.Client;
import com.iwbfly.myhttp.exceptions.RetryableException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 21:30
 **/
public class TestRetryClient extends BaseClientTest {

    public final static String EXPECTED = "OK";

    private RetryClient retryClient;

    public TestRetryClient(Client client) {
        super(client);
        this.retryClient = configuration.createInstance(RetryClient.class);
    }

    @Test
    public void post1() throws InterruptedException {
        MockResponse mockResponse = new MockResponse().setBody(EXPECTED);
        mockResponse.setHeadersDelay(1000, TimeUnit.MILLISECONDS);
        server.enqueue(mockResponse);
        try {
            retryClient.post1("xiaoming", "123456");
        } catch (RetryableException e) {
            System.out.println(e.getCause());
        }
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");

    }

}
