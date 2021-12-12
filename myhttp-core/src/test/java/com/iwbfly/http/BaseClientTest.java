package com.iwbfly.http;

import com.iwbfly.myhttp.Client;
import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.http.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @auther: pangyajun
 * @create: 2021/12/4 14:32
 **/

@RunWith(Parameterized.class)
public abstract class BaseClientTest {

    protected Client client;
    protected static Myhttp.Builder configuration;

    @Rule
    public MockWebServer server = new MockWebServer();

    @BeforeClass
    public static void beforeClass() throws Exception {
        configuration = Myhttp.builder().build();
    }

    public BaseClientTest(Client client) {
        this.client = client;
        configuration.client(client);
        configuration.setVariableValue("port", server.getPort());
        configuration.setVariableValue("localhostUrl", "http://localhost:" + server.getPort());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> t(){
        return Arrays.asList(
                new Client[][] {{new OkHttpClient()}});
    }

}
