package com.plms.rpc.provider;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.extension.SPI;

/**
 * @Author bigboss
 * @Date 2021/10/27 18:59
 */
public interface ServiceProvider {

    void addService(RpcConfig rpcConfig);

    Object getService(String serviceName);

    void publishService(RpcConfig rpcConfig);
}
