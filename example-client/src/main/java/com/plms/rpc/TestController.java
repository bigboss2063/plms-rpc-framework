package com.plms.rpc;

import com.plms.rpc.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @Author bigboss
 * @Date 2021/11/23 12:49
 */
@Component
public class TestController {

    @RpcReference(version = "Version1", group = "Test1")
    private TestService testService;

    public void test() {
        this.testService.hello();
    }
}
