package com.plms.rpc.remoting.sever.hooker;

import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.register.zk.util.CuratorUtils;
import com.plms.rpc.remoting.sever.NettyRpcSever;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @Author bigboss
 * @Date 2021/10/28 21:43
 */
@Slf4j
public class ServiceShutDownHooker {
    private static final ServiceShutDownHooker SERVICE_SHUT_DOWN_HOOKER = new ServiceShutDownHooker();

    public static ServiceShutDownHooker getServiceShutDownHooker() {
        return SERVICE_SHUT_DOWN_HOOKER;
    }

    public void clearAllServices() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcSever.PORT);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
                log.info("clear all nodes of [{}]", inetSocketAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }));
    }
}
