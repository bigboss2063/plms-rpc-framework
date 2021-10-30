package com.plms.rpc;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.remoting.sever.NettyRpcSever;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:33
 */
@Slf4j
public class RpcServer {
    @SneakyThrows
    public static void main(String[] args) {
        NettyRpcSever nettyRpcSever = new NettyRpcSever(9999); // 配置服务端口号
        Class<?> TestServiceImpl1target = Class.forName("com.plms.rpc.TestServiceImpl1");
        Object testServiceImpl1 = TestServiceImpl1target.newInstance();
        Class<?> TestServiceImpl2target = Class.forName("com.plms.rpc.TestServiceImpl2");
        Object testServiceImpl2 = TestServiceImpl2target.newInstance();
        Integer weight = 3;
        RpcConfig rpcConfig1 = RpcConfig.builder()
                .service(testServiceImpl1)
                .weight(weight) // 服务权值
                .group("Test1")
                .version("Version1")
                .build();
        RpcConfig rpcConfig2 = RpcConfig.builder()
                .service(testServiceImpl2)
                .weight(weight) // 服务权值
                .group("Test2")
                .version("Version1")
                .build();
        nettyRpcSever.registerService(rpcConfig1); // 注册服务
        nettyRpcSever.registerService(rpcConfig2); // 注册服务
        nettyRpcSever.start(); // 启动服务
    }
}
