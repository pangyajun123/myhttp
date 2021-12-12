package com.iwbfly.http;

import com.iwbfly.http.client.LoggerFilterClient;
import com.iwbfly.mock.MockServerRequest;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.Client;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 20:27
 **/
public class TestLoggerFilter extends BaseClientTest{
    public final static String EXPECTED = "OK";

    private LoggerFilterClient loggerFilterClient;
    public TestLoggerFilter(Client client) {
        super(client);
        this.loggerFilterClient = configuration.createInstance(LoggerFilterClient.class);
    }

    @Test
    public void post1() {
        server.enqueue(new MockResponse().setBody("{\"username\":\"xiaoming\",\"password\":\"123456\"}"));
        assertThat(loggerFilterClient.post1("xiaoming", "123456"))
                .isNotNull();
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");
    }
}
