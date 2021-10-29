package com.plms.rpc;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.remoting.sever.NettyRpcSever;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:33
 */
public class RpcServer {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        NettyRpcSever nettyRpcSever = new NettyRpcSever(9999); // 配置服务端口号
        Class<?> target = Class.forName("com.plms.rpc.TestServiceImpl");
        Object o = target.newInstance();
        Integer weight = 3;
        RpcConfig rpcConfig = RpcConfig.builder()
                .service(o)
                .weight(weight) // 服务权值
                .build();
        nettyRpcSever.registerService(rpcConfig); // 注册服务
        nettyRpcSever.start(); // 启动服务
    }
}
