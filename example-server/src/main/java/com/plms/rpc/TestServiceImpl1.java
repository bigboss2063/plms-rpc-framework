package com.plms.rpc;

import com.plms.rpc.annotation.RpcService;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:33
 */
@RpcService(version = "Version1", group = "Test1", weight = 3)
public class TestServiceImpl1 implements TestService{
    @Override
    public void hello() {
        System.out.println("my promise");
    }
}
