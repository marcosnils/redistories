import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConsumerTest
{
    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();

    @org.junit.Test
    public void testConsumer() throws IOException {
        environmentVariables.set("REDIS_URL", "redis://localhost:6379");
        System.out.println(System.getenv("REDIS_URL"));
        Jedis j = new Jedis();
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
        Map<String, String> params = new HashMap<>();
        params.put("message", "test");
        StreamEntryID resp = j.xadd("stories", null, params);
        Consumer.handler(exchange);
        assertEquals("[\"test\"]",new String(((ByteArrayOutputStream) exchange.getResponseBody()).toByteArray()));
    }
}
