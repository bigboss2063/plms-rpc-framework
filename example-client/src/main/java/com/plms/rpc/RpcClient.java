package com.plms.rpc;

import com.plms.rpc.annotation.RpcReference;
import com.plms.rpc.annotation.RpcScan;
import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.proxy.RpcClientProxy;
import com.plms.rpc.remoting.client.NettyRpcClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:32
 */
@RpcScan(basePackage = {"com.plms.rpc"})
public class RpcClient {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcClient.class);
        TestController testController = (TestController) applicationContext.getBean("testController");
        testController.test();
    }
}
