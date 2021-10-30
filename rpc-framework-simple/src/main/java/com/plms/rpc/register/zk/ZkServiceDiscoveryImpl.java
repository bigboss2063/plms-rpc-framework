package com.plms.rpc.register.zk;

import com.esotericsoftware.minlog.Log;
import com.plms.rpc.exception.RpcException;
import com.plms.rpc.extension.ExtensionLoader;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.loadbalance.LoadBalance;
import com.plms.rpc.loadbalance.impl.RandomLoadBalance;
import com.plms.rpc.loadbalance.impl.RoundRobinLoadBalance;
import com.plms.rpc.register.ServiceDiscovery;
import com.plms.rpc.register.zk.util.CuratorUtils;
import com.plms.rpc.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * @Author bigboss
 * @Date 2021/10/26 21:34
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("default");
    }

    @Override
    public InetSocketAddress discoveryService(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        if (serviceUrlList == null) {
            throw new RpcException("service [" + serviceName + "] not exist");
        }
        String serviceUrl = loadBalance.serverLoadBalance(rpcRequest, serviceUrlList);
        log.info("serviceUrl:{}", serviceUrl);
        serviceUrl = removeWeight(serviceUrl);
        return new InetSocketAddress(serviceUrl.split(":")[0], Integer.parseInt(serviceUrl.split(":")[1]));
    }

    private String removeWeight(String serviceUrl) {
        return serviceUrl.split("#")[0];
    }
}
