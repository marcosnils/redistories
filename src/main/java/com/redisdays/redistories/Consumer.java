package com.redisdays.redistories;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class Consumer {

    public static void main(String[] args) {
        Jedis j = new Jedis();


        Map.Entry<String, StreamEntryID> e = new AbstractMap.SimpleEntry("stories", new StreamEntryID(System.currentTimeMillis() - 30000, 0));
        List<Map.Entry<String, List<StreamEntry>>> resp = j.xread(10, 1000, e);

        if (resp.size() > 0) {
            for (StreamEntry se : resp.get(0).getValue()) {
                System.out.println(se.getFields().get("message"));
            }
        }
    }
}
