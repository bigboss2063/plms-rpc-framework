package com.plms.rpc.register;

import com.plms.rpc.extension.SPI;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @Author bigboss
 * @Date 2021/10/26 21:33
 */
@SPI
public interface ServiceDiscovery {
    InetSocketAddress discoveryService(RpcRequest rpcRequest);
}
