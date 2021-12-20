package com.plms.rpc.register.redis.util;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.enums.RpcConfigEnum;
import com.plms.rpc.util.PropertiesFileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bigboss
 * @Date 2021/10/30 19:29
 */
@Slf4j
public class RedisUtils {

    private static final Jedis JEDIS;
    private static final int MAX_IDLE = 20;
    private static final int MAX_TOTAL = 20;
    private static final int MIN_IDLE = 10;
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final String DEFAULT_REDIS_ADDRESS = "159.75.92.63:6379";
    private static final String DEFAULT_REDIS_PASSWORD = "212009spurs";
    public static final String REDIS_REGISTER_ROOT_PATH = "/plms-rpc";

    static {
        /*Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String redisAddress = properties != null && properties.getProperty(RpcConfigEnum.REDIS_ADDRESS.getPropertyValue()) != null
                ? properties.getProperty(RpcConfigEnum.REDIS_ADDRESS.getPropertyValue()) : DEFAULT_REDIS_ADDRESS;
        String password = properties != null && properties.getProperty(RpcConfigEnum.REDIS_PASSWORD.getPropertyValue()) != null
                ? properties.getProperty(RpcConfigEnum.REDIS_PASSWORD.getPropertyValue()) : DEFAULT_REDIS_PASSWORD;*/
        String redisAddress = DEFAULT_REDIS_ADDRESS;
        String password = DEFAULT_REDIS_PASSWORD;
        GenericObjectPoolConfig<Jedis> jedisConfig = new GenericObjectPoolConfig<>();
        jedisConfig.setMaxIdle(MAX_IDLE);
        jedisConfig.setMaxTotal(MAX_TOTAL);
        jedisConfig.setMinIdle(MIN_IDLE);
        JedisPool jedisPool = new JedisPool(jedisConfig, redisAddress.split(":")[0],
                Integer.parseInt(redisAddress.split(":")[1]), 3000, password);
        JEDIS = jedisPool.getResource();
    }

    private RedisUtils() {}

    public static Jedis getJedis() {
        return JEDIS;
    }

    public static void createNodes(RpcConfig rpcConfig, InetSocketAddress address) {
        String serviceSetName = REDIS_REGISTER_ROOT_PATH + rpcConfig.getRpcServiceName();
        String serviceAddress = address.toString() + "#" + rpcConfig.getWeight();
        String path  = serviceSetName + serviceAddress;
        try {
            if (REGISTERED_PATH_SET.contains(path) || JEDIS.sismember(serviceSetName, serviceAddress)) {
                log.info("节点 [{}] 已经存在", path);
            } else {
                JEDIS.sadd(serviceSetName, serviceAddress);
                log.info("节点 [{}] 创建成功", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("节点 [{}] 创建失败", path);
        }
    }

}
