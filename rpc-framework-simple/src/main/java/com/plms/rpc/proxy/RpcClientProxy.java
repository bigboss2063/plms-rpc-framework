package com.plms.rpc.proxy;

import com.plms.rpc.remoting.client.NettyRpcClient;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author bigboss
 * @Date 2021/10/27 13:23
 */
public class RpcClientProxy implements InvocationHandler {

    private NettyRpcClient nettyRpcClient;

    public RpcClientProxy(NettyRpcClient nettyRpcClient) {
        this.nettyRpcClient = nettyRpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .methodName(method.getName())
                .serviceName(method.getDeclaringClass().getCanonicalName())
                .parameterTypes(method.getParameterTypes())
                .parameterValues(args)
                .build();
        return nettyRpcClient.sendRpcRequest(rpcRequest);
    }
}
