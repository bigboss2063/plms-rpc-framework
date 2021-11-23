package com.plms.rpc.register.redis;

import com.plms.rpc.register.ServiceDiscovery;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @Author bigboss
 * @Date 2021/11/23 14:43
 */
public class RedisServiceDiscoveryImpl implements ServiceDiscovery {
    @Override
    public InetSocketAddress discoveryService(RpcRequest rpcRequest) {
        return null;
    }
}
