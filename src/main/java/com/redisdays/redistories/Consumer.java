package com.redisdays.redistories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Consumer {

    private static Jedis j;

    static {
        j = new Jedis(System.getenv("REDIS_URL"));
    }

    public static void fetchMessages(final HttpExchange exchange) throws IOException {
        Map.Entry<String, StreamEntryID> query = new AbstractMap.SimpleEntry("stories", new StreamEntryID(System.currentTimeMillis() - 30000, 0));
        List<Map.Entry<String, List<StreamEntry>>> resp = j.xread(10, 1000, query);
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
