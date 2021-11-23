package com.plms.rpc.provider.impl;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.exception.RpcException;
import com.plms.rpc.extension.ExtensionLoader;
import com.plms.rpc.provider.ServiceProvider;
import com.plms.rpc.register.ServiceRegister;
import com.plms.rpc.remoting.sever.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bigboss
 * @Date 2021/10/27 19:01
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    private Map<String, Object> serviceMap;
    private Set<String> registerService;
    private ServiceRegister serviceRegister;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registerService = ConcurrentHashMap.newKeySet();
        serviceRegister = ExtensionLoader.getExtensionLoader(ServiceRegister.class).getExtension("zkRegister");
    }

    @Override
    public void addService(RpcConfig rpcConfig) {
        String serviceName = rpcConfig.getRpcServiceName();
        if (registerService.contains(serviceName)) {
            return;
        }
        registerService.add(serviceName);
        serviceMap.put(serviceName, rpcConfig.getService());
        log.info("服务 [{}] 已经被添加进服务中心", serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (Objects.isNull(service)) {
            throw new RpcException("service [" + serviceName + "] not exist");
        }
        return service;
    }

    @Override
    public void publishService(RpcConfig rpcConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcConfig);
            serviceRegister.registerService(rpcConfig, new InetSocketAddress(host, 9999));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
