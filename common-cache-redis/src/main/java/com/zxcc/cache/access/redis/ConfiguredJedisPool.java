package com.zxcc.cache.access.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * redis pool
 * Created by xuanzh.cc on 2016/7/23.
 */
@Component
public class ConfiguredJedisPool {

    private String address;

    private int port;

    private JedisPool jedisPool;

    @PostConstruct
    public void init(){
        this.jedisPool = new JedisPool(this.address, this.port);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取 jedis
     * @return
     */
    public Jedis getResource() {
        return this.jedisPool.getResource();
    }

    @PreDestroy
    public void destory(){
        if(this.jedisPool != null && !this.jedisPool.isClosed()) {
            this.jedisPool.close();
        }
    }
}
