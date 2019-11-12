package com.redisdays.redistories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Consumer {

    private static Jedis j;

    static {
        j = new Jedis(System.getenv("REDIS_URL"));
    }

    public static void main(String[] args) throws IOException {

        final HttpExchange exchange = new HttpExchange() {
            final OutputStream os = new ByteArrayOutputStream();
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
                return null;
            }

            @Override
            public OutputStream getResponseBody() {
                return os;
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
        fetchMessages(exchange);
        System.out.println(new String(((ByteArrayOutputStream) exchange.getResponseBody()).toByteArray()));

    }

    public static void fetchMessages(final HttpExchange exchange) throws IOException {
        Map.Entry<String, StreamEntryID> e = new AbstractMap.SimpleEntry("stories", new StreamEntryID(System.currentTimeMillis() - 30000, 0));
        List<Map.Entry<String, List<StreamEntry>>> resp = j.xread(10, 1000, e);
        List<String> response = new ArrayList<>();
        if (resp.size() > 0) {
            for (StreamEntry se : resp.get(0).getValue()) {
                response.add(se.getFields().get("message"));
            }
        }
        OutputStream os = exchange.getResponseBody();
        ObjectMapper Obj = new ObjectMapper();
        os.write(Obj.writeValueAsString(response).getBytes());
        os.close();
    }

}
