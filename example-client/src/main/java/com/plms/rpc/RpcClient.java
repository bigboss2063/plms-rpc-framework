package com.plms.rpc;

import com.plms.rpc.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

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
