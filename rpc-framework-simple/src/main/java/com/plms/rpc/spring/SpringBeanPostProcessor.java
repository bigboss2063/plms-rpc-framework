package com.plms.rpc.spring;

import com.esotericsoftware.minlog.Log;
import com.plms.rpc.annotation.RpcReference;
import com.plms.rpc.annotation.RpcService;
import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.provider.ServiceProvider;
import com.plms.rpc.provider.impl.ZkServiceProviderImpl;
import com.plms.rpc.proxy.RpcClientProxy;
import com.plms.rpc.remoting.client.NettyRpcClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 自定义 BeanPostProcessor，来自动消费和注册服务
 * @Author bigboss
 * @Date 2021/11/23 11:32
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;

    private final NettyRpcClient rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcClient = new NettyRpcClient();
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 如果这个 Bean 被 @RpcService 标注，那么在该方法中将服务注册出去
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with [{}]",
                    bean.getClass().getName(),
                    RpcService.class.getCanonicalName());
            // 获取 RpcService 里的版本号、实现类、权值
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcConfig rpcConfig = RpcConfig.builder()
                    .service(bean)
                    .group(rpcService.group())
                    .version(rpcService.version())
                    .weight(rpcService.weight())
                    .build();
            serviceProvider.publishService(rpcConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        // 获取到 Bean 上的所有属性
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 找到被 @RpcReference 标注的属性
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcConfig rpcConfig = RpcConfig.builder()
                        .group(rpcReference.group())
                        .version(rpcReference.version())
                        .build();
                // 获取远程调用的代理对象
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcConfig);
                Object proxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    // 将代理对象赋值到被 @RpcReference 标注的属性上，让客户端可以直接调用远程服务
                    declaredField.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
