package com.plms.rpc;

import com.plms.rpc.annotation.RpcService;

/**
 * @Author bigboss
 * @Date 2021/10/30 17:25
 */
@RpcService(version = "Version1", group = "Test2", weight = 5)
public class TestServiceImpl2 implements TestService{
    @Override
    public void hello() {
        System.out.println("my big boss");;
    }
}
