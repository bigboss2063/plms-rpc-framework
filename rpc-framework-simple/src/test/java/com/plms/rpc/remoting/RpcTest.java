package com.plms.rpc.remoting;

import com.plms.rpc.TestService;
import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.proxy.RpcClientProxy;
import com.plms.rpc.remoting.client.NettyRpcClient;
import com.plms.rpc.remoting.sever.NettyRpcSever;
import org.junit.Test;

/**
 * @Author bigboss
 * @Date 2021/10/27 15:31
 */
public class RpcTest {

    @Test
    public void serverTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        NettyRpcSever nettyRpcSever = new NettyRpcSever(7777);
        Class<?> target = Class.forName("com.plms.rpc.TestServiceImpl");
        Object o = target.newInstance();
        RpcConfig rpcConfig = RpcConfig.builder()
                .service(o)
                .build();
        nettyRpcSever.registerService(rpcConfig);
        nettyRpcSever.start();
    }

    @Test
    public void clientTest() {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient);
        TestService proxy = rpcClientProxy.getProxy(TestService.class);
        System.out.println(proxy.hello());
    }
}
