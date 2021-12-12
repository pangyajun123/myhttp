package com.iwbfly.http;

import com.iwbfly.http.client.BaseInterceptorClient;
import com.iwbfly.mock.MockServerRequest;
import com.iwbfly.myhttp.Client;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 20:27
 **/
public class TestInterceptor extends BaseClientTest{
    final static String EXPECTED = "OK";

    private BaseInterceptorClient baseInterceptorClient;
    public TestInterceptor(Client client) {
        super(client);
        this.baseInterceptorClient = configuration.createInstance(BaseInterceptorClient.class);
    }

    @Test
    public void post1() {
        server.enqueue(new MockResponse().setBody(EXPECTED));
        assertThat(baseInterceptorClient.post1("xiaoming", "123456"))
                .isNotNull()
                .isEqualTo(EXPECTED);
        MockServerRequest.mockRequest(server)
                .assertMethodEquals("POST")
                .assertPathEquals("/test")
                .assertHeaderEquals("Content-Type", "application/x-www-form-urlencoded")
                .assertHeaderEquals("baseOnInvokeMethodHeader", "666666")
                .assertHeaderEquals("baseBeforeExecuteHeader", "666666")
                .assertHeaderEquals("methodOnInvokeMethodHeader", "666666")
                .assertHeaderEquals("methodBeforeExecuteHeader", "666666")
                .assertBodyNotEmpty()
                .assertBodyEquals("username=xiaoming&password=123456&baseOnInvokeMethodAdd=666666&methodOnInvokeMethodAdd=666666&baseBeforeExecuteAdd=666666&methodBeforeExecuteAdd=666666");

    }

}
