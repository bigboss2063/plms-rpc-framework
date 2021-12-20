package com.plms.rpc.extension;

import cn.hutool.core.lang.Assert;
import com.plms.rpc.register.ServiceDiscovery;
import com.plms.rpc.serialize.Serializer;
import org.junit.jupiter.api.Test;

/**
 * @Author bigboss
 * @Date 2021/11/23 20:09
 */
public class ExtensionTest {

    @Test
    public void testExtension() {
        ServiceDiscovery serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("default");
        Assert.notNull(serviceDiscovery);
        System.out.println(serviceDiscovery.getClass().getName());
    }
}
