package com.plms.rpc;

import com.plms.rpc.annotation.RpcScan;
import com.plms.rpc.remoting.sever.NettyRpcServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:33
 */
@Slf4j
@RpcScan(basePackage = {"com.plms.rpc"})
public class RpcServer {
    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcServer.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        nettyRpcServer.start(); // 启动服务
    }
}
