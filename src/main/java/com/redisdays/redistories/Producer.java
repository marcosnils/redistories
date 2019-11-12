package com.redisdays.redistories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class Producer
{
    private static Jedis j;

    static {
        j = new Jedis(System.getenv("REDIS_URL"));
    }

    public static void main( String[] args ) throws IOException {

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
        produceMessage(he);
    }

    public static void produceMessage(final HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(exchange.getRequestBody(), Map.class);
        Map<String, String> params = new HashMap<>();
        params.put("message", (String) jsonMap.get("message"));
        StreamEntryID resp = j.xadd("stories", null, params);
    }
}
