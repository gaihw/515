package com.zmj.demo.service.impl.plugin;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis相关操作，本文示例只需要使用到get，set和delete操作，都是简单的使用RedisTemplate
 */
@Service
public class RedisService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public void set(String key, Object value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer =new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void set(String key, Object value,long time) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer =new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value, time, TimeUnit.SECONDS);
    }

    public void addValue(String key, Object value) throws Exception {
        redisTemplate.boundValueOps(key).set(value);
    }

    public Object get(String key) {
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    public void addValue(String key, Object value, Long timeout) throws Exception {

        redisTemplate.boundValueOps(key).set(value, timeout, TimeUnit.DAYS);
    }

    public void addValueSecond(String key, Object value, Long timeout) throws Exception {
        redisTemplate.boundValueOps(key).set(value, timeout, TimeUnit.SECONDS);
    }

    public Object getValue(String key) throws Exception {
        return redisTemplate.boundValueOps(key).get();
    }

    public void addList(String key, List<Object> list, Long timeout) throws Exception {
        redisTemplate.boundValueOps(key).set(list, timeout, TimeUnit.DAYS);
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList(String key) throws Exception {
        return (List<Object>) redisTemplate.boundValueOps(key).get();
    }

    public void addMap(String key, Map<String, Object> map, Long timeout) throws Exception {
        redisTemplate.boundValueOps(key).set(map, timeout, TimeUnit.DAYS);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(String key) throws Exception {
        return (Map<String, Object>) redisTemplate.boundValueOps(key).get();
    }

    public Long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

}
