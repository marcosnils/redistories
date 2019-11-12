package com.redisdays.redistories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Producer
{
    private static Jedis j;

    static {
        j = new Jedis(System.getenv("REDIS_URL"));
    }

    public static void handler(final HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(exchange.getRequestBody(), Map.class);
        Map<String, String> params = new HashMap<>();
        params.put("message", (String) jsonMap.get("message"));
        StreamEntryID resp = j.xadd("stories", null, params);
    }
}
