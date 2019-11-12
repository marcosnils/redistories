package com.redisdays.redistories;

import com.sun.net.httpserver.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import javax.net.ssl.SSLSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ProducerTest
{
    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();

    @org.junit.Test
    public void testProducer() throws IOException {
        environmentVariables.set("REDIS_URL", "redis://localhost:6379");
        System.out.println(System.getenv("REDIS_URL"));
        Jedis j = new Jedis();
        HttpExchange he = new HttpsExchange() {
            @Override
            public SSLSession getSSLSession() {
                return null;
            }

            @Override
            public Headers getRequestHeaders() {
                return null;
            }

            @Override
            public Headers getResponseHeaders() {
                return null;
            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public String getRequestMethod() {
                return null;
            }

            @Override
            public HttpContext getHttpContext() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getRequestBody() {
                return new ByteArrayInputStream("{\"message\": \"Hello there!\"}".getBytes());
            }

            @Override
            public OutputStream getResponseBody() {
                return null;
            }

            @Override
            public void sendResponseHeaders(int i, long l) throws IOException {

            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public int getResponseCode() {
                return 0;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void setStreams(InputStream inputStream, OutputStream outputStream) {

            }

            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
        };
        Producer.handler(he);
        Map.Entry<String, StreamEntryID> query = new AbstractMap.SimpleEntry("stories", new StreamEntryID(System.currentTimeMillis() - 30000, 0));
        List<Map.Entry<String, List<StreamEntry>>> resp = j.xread(10, 1000, query);
        assertEquals(1, resp.size());
    }
}
