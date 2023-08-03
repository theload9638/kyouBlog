package com.kyou.blog.background.config;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author CC
 * time 2023-07-20
 * description
 */
public class JsonRedisSerialize<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;
    public JsonRedisSerialize(Class<T> clazz){
        super();
        this.clazz=clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t==null) {
            return new byte[0];
        }
        return JSONUtil.toJsonStr(t).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes==null||bytes.length<=0) {
            return null;
        }
        String s = new String(bytes, DEFAULT_CHARSET);
        return JSONUtil.toBean(s,clazz);
    }
}
