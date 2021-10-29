package com.plms.rpc.register;

import com.plms.rpc.config.RpcConfig;

import java.net.InetSocketAddress;

/**
 * @Author bigboss
 * @Date 2021/10/26 21:32
 */
public interface ServiceRegister {

    void registerService(RpcConfig rpcConfig, InetSocketAddress address) throws Exception;
}
