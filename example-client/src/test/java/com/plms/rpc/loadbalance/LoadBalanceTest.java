package com.plms.rpc.loadbalance;

import com.plms.rpc.TestService;
import com.plms.rpc.proxy.RpcClientProxy;
import com.plms.rpc.register.ServiceDiscovery;
import com.plms.rpc.register.zk.ZkServiceDiscoveryImpl;
import com.plms.rpc.remoting.client.NettyRpcClient;
import com.plms.rpc.remoting.dto.RpcRequest;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @Author bigboss
 * @Date 2021/10/29 19:28
 */
public class LoadBalanceTest {

    @Test
    public void loadBalanceTest() {
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .methodName("hello")
                .serviceName("com.plms.rpc.TestService")
                .parameterTypes(null)
                .parameterValues(null)
                .build();
        ServiceDiscovery serviceDiscovery = new ZkServiceDiscoveryImpl();
        for (int i = 0; i < 12; i++) {
            serviceDiscovery.discoveryService(rpcRequest);
        }
    }
}
