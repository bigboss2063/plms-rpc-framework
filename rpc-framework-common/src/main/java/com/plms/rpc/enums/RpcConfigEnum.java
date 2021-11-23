package com.plms.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author bigboss
 * @Date 2021/10/29 13:25
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcConfigEnum {

    /**
     * RPC配置文件路径
     */
    RPC_CONFIG_PATH("config/plms-rpc.properties"),

    /**
     * zookeeper地址
     */
    ZOOKEEPER_ADDRESS("zookeeper.address"),

    /**
     * redis地址
     */
    REDIS_ADDRESS("redis.address"),

    /**
     * redis密码
     */
    REDIS_PASSWORD("redis.password");

    private final String propertyValue;
}
