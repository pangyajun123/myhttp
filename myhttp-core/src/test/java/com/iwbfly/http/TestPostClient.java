package com.iwbfly.http;

import com.iwbfly.http.client.PostClient;
import com.iwbfly.mock.MockServerRequest;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.Client;
import okhttp3.mockwebserver.MockResponse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @auther: pangyajun
 * @create: 2021/12/5 12:30
 **/
public class TestPostClient extends BaseClientTest {
    public final static String EXPECTED = "OK";

    private PostClient postClient;

    public TestPostClient(Client client) {
        super(client);
        this.postClient = configuration.createInstance(PostClient.class);
    }

    @Test
    public void post1() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post1("xiaoming", "123456"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");
    }

    @Test
    public void post2() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post2(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertBodyNotEmpty()
                .assertBodyEquals("username=xiaoming&password=123456");
    }

    @Test
    public void post3() throws CloneNotSupportedException {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        List<User> list = new ArrayList<>();
        User user = new User("xiaoming", "123456");
        list.add(user);
        list.add(user.clone());
        assertThat(postClient.post3(list, "666666"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertHeaderEquals("token","666666")
                .assertBodyNotEmpty()
                .assertBodyEquals("[{\"username\":\"xiaoming\",\"password\":\"123456\"},{\"username\":\"xiaoming\",\"password\":\"123456\"}]");
    }

    @Test
    public void post4() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post4(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");
    }

    @Test
    public void post5() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post5(new User("xiaoming", "123456")))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertHeaderEquals("token","666666")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");
    }

    @Test
    public void post6() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post6("xiaoming", "123456"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/x-www-form-urlencoded")
                .assertBodyNotEmpty()
                .assertBodyEquals("username=xiaoming&password=123456");
    }

    @Test
    public void post7() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(postClient.post7(new User("xiaoming", "123456"),"666666","666666"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Accept-Charset", "UTF-8")
                .assertHeaderEquals("Accept", "text/plain")
                .assertHeaderEquals("Content-Type", "application/json")
                .assertHeaderEquals("token1","666666")
                .assertHeaderEquals("token2","123456")
                .assertHeaderEquals("token3","666666")
                .assertBodyNotEmpty()
                .assertBodyEquals("{\"username\":\"xiaoming\",\"password\":\"123456\"}");
    }
}
