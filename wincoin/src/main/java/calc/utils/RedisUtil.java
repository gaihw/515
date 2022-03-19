package calc.utils;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    public static Jedis jedis(int index){
        //链接redis
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("123456");
        jedis.select(index);
        return jedis;
    }
}
