package com.plms.rpc;

/**
 * @Author bigboss
 * @Date 2021/10/28 14:33
 */
public class TestServiceImpl implements TestService{
    @Override
    public String hello() {
        return "my promise";
    }
}
