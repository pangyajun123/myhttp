package com.iwbfly.http;

import com.iwbfly.mock.MockServerRequest;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.Client;
import com.iwbfly.http.client.GetClient;

import com.iwbfly.myhttp.http.Response;
import okhttp3.mockwebserver.MockResponse;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @auther: pangyajun
 * @create: 2021/12/4 13:54
 **/
public class TestGetClient extends BaseClientTest {

    public final static String EXPECTED = "OK";

    private final GetClient getClient;

    public TestGetClient(Client client) {
        super(client);
        getClient = configuration.createInstance(GetClient.class);
    }

    @Test
    public void get1() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(getClient.get1("xiaoming", "123456"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456");
    }

    @Test
    public void get2() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(getClient.get2("xiaoming", "123456", "123456"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("token", "123456")
                .assertHeaderEquals("token1", "xiaoming")
                .assertHeaderEquals("token2", "123456")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456");
    }

    @Test
    public void get3() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(getClient.get3(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("token", "666666")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456")
                .assertQueryEquals("data1", "xiaoming")
                .assertQueryEquals("data2", "123456");
    }

    @Test
    public void get4() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(getClient.get4(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("token", "666666")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456");

    }

    @Test
    public void get5() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(getClient.get5(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("token", "xiaoming")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456");

    }

    @Test
    public void get6() throws IOException {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        Response response = getClient.get6(new User("xiaoming", "123456"));
        assertThat(response.status()).isEqualTo(200);
        assertThat(response.headers()).isNotNull();
        assertThat(response.body().string()).isEqualTo(EXPECTED);

        MockServerRequest.mockRequest(server)
                .assertMethodEquals("GET")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("token", "666666")
                .assertQueryEquals("username", "xiaoming")
                .assertQueryEquals("password", "123456");

    }
}
