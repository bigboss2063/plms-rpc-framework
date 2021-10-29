package com.plms.rpc;

import com.plms.rpc.proxy.RpcClientProxy;
import com.plms.rpc.remoting.client.NettyRpcClient;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:32
 */
public class RpcClient {
    public static void main(String[] args) throws IOException {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient);
        TestService proxy = rpcClientProxy.getProxy(TestService.class); // 获取服务接口类的代理类实例
        proxy.hello(); // 远程调用服务
    }
}
