package com.plms.rpc.rigster;

import com.plms.rpc.TestService;
import com.plms.rpc.register.ServiceRegister;
import com.plms.rpc.register.zk.ZkServiceRegisterImpl;
import com.plms.rpc.register.zk.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author bigboss
 * @Date 2021/10/26 15:58
 */
public class zkTest {

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;

    @Test
    public void zkRegistrationCenter() throws Exception {
//        ServiceRegister serviceRegister = new ZkServiceRegisterImpl();
//        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 7777);
//        TestService testService = new testserviceImpl
//        serviceRegister.registerService(testService.getClass().getInterfaces()[0].getCanonicalName(), givenInetSocketAddress);
//        ServiceDiscovery zkServiceDiscovery = new ZkServiceDiscoveryImpl();
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(testService.getClass().getInterfaces()[0].getCanonicalName())
//                .requestId(UUID.randomUUID().toString())
//                .build();
//        InetSocketAddress acquiredInetSocketAddress = zkServiceDiscovery.discoveryService(rpcRequest);
//        assertEquals(givenInetSocketAddress.toString(), acquiredInetSocketAddress.toString());
    }

    @Test
    public void zkUtilsTest() {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> childrenNodes = CuratorUtils.getChildrenNodes(zkClient, "com.plms.rpc.com.plms.rpc.TestService");
        System.out.println(childrenNodes.toString());
    }
}
