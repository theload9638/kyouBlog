package com.kyou.blog.background.webUtil;

import cn.hutool.json.JSONUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author CC
 * time 2023-07-15
 * description
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public StringRedisTemplate get(){
        return stringRedisTemplate;
    }
    public void setVal(String key, Object data){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForValue()
                    .set(key, String.valueOf(data));
        }else {
            stringRedisTemplate.opsForValue()
                    .set(key,JSONUtil.toJsonStr(data));
        }
    }
    public void setVal(String key, Object data, Duration duration){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForValue()
                    .set(key, String.valueOf(data),duration);
        }else {
            stringRedisTemplate.opsForValue()
                    .set(key,JSONUtil.toJsonStr(data),duration);
        }
    }
    public void setValForSet(String key, Object data){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForSet()
                    .add(key, String.valueOf(data));
        }else {
            stringRedisTemplate.opsForSet()
                    .add(key,JSONUtil.toJsonStr(data));
        }
    }
    public void setValForSet(String key, Object data, long time, TimeUnit unit){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForSet()
                    .add(key, String.valueOf(data));
        }else {
            stringRedisTemplate.opsForSet()
                    .add(key,JSONUtil.toJsonStr(data));
        }
        stringRedisTemplate.expire(key,time,unit);
    }
    public void addValForSet(String key, Object data){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForSet()
                    .add(key, String.valueOf(data));
        }else {
            stringRedisTemplate.opsForSet()
                    .add(key,JSONUtil.toJsonStr(data));
        }
    }
    public void delValForSet(String k,Object data){
        if(data instanceof String || data instanceof Number){
            stringRedisTemplate.opsForSet()
                    .remove(k,String.valueOf(data));
        }else {
            stringRedisTemplate.opsForSet()
                    .remove(k,JSONUtil.toJsonStr(data));
        }
    }
    public Set<String> keys(String pattern){
        return stringRedisTemplate.keys(pattern);
    }
    public void del(String key){
        stringRedisTemplate.delete(key);
    }
    public void del(Collection<String> k){
        stringRedisTemplate.delete(k);
    }
    public String getVal(String key){
        return stringRedisTemplate.opsForValue()
                .get(key);
    }
    public boolean getSetVal(String k,Object data){
        if(data instanceof String || data instanceof Number){
            Boolean member = stringRedisTemplate.opsForSet().isMember(k, String.valueOf(data));
            return WebUtil.isTrue(member);
        }else {
            Boolean member = stringRedisTemplate.opsForSet().isMember(k, JSONUtil.toJsonStr(data));
            return WebUtil.isTrue(member);
        }
    }
    public Long getSetSize(String k){
        return stringRedisTemplate.opsForSet().size(k);
    }
    public Set<String> getSetMember(String k){
        return stringRedisTemplate.opsForSet().members(k);
    }
    public Object getValBean(String key,Class clz){
        String json = stringRedisTemplate.opsForValue()
                .get(key);
        if (StringUtils.hasText(json)) {
            if(clz==String.class){
                return json;
            }else {
                return JSONUtil.toBean(json,clz);
            }
        }
        return null;
    }
    public void delBatch(String pattern){
        Set<String> keys = stringRedisTemplate.keys(pattern);
        stringRedisTemplate.delete(keys);
    }
    public SetOperations<String, String> set(){
        return stringRedisTemplate.opsForSet();
    }
    public ValueOperations<String, String> val(){
        return stringRedisTemplate.opsForValue();
    }
    public HashOperations<String, Object, Object> hash(){
        return stringRedisTemplate.opsForHash();
    }





}
