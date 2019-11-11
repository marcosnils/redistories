package com.redisdays.redistories;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

import java.util.HashMap;
import java.util.Map;


public class App
{
    public static void main( String[] args )
    {

        Jedis j = new Jedis();

        Map<String, String> params = new HashMap<>();
        params.put("message", "Hello there!");
        StreamEntryID resp = j.xadd("stories", null, params);
        System.out.println(resp);
    }
}
