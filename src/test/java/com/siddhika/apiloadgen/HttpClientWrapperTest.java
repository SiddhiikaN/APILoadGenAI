package com.siddhika.apiloadgen;

import com.siddhika.apiloadgen.http.HttpClientWrapper;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class HttpClientWrapperTest {
    private HttpServer server;
    private HttpClientWrapper wrapper;
    private String url;

    @BeforeEach
    void setup() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);

        int port = server.getAddress().getPort();

        url = "http://localhost:" + port + "/test";
        
        server.createContext("/test", exchange -> {
            String response = "Hello from test server";
            
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        wrapper = new HttpClientWrapper();
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void shouldSendPostRequestAndReceiveResponse() throws Exception{
        HttpResponse<String> response = wrapper.post(url, "{}");

        assertEquals(200, response.statusCode());
        assertEquals("Hello from test server", response.body());
    }
}
