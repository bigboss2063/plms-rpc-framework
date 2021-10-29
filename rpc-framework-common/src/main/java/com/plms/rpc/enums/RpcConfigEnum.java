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

    RPC_CONFIG_PATH("config/plms-rpc.properties"),

    ZOOKEEPER_ADDRESS("zookeeper.address");

    private final String propertyValue;
}
