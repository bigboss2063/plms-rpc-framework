package com.plms.rpc.register.zk;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.register.ServiceRegister;
import com.plms.rpc.register.zk.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @Author bigboss
 * @Date 2021/10/26 21:33
 */
public class ZkServiceRegisterImpl implements ServiceRegister {

    @Override
    public void registerService(RpcConfig rpcConfig, InetSocketAddress address) throws Exception {
        String path = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcConfig.getServiceName() + address.toString() + "#" + rpcConfig.getWeight();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, path);
    }
}
