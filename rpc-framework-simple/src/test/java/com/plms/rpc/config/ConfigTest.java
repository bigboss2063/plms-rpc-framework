package com.plms.rpc.config;

import com.plms.rpc.enums.RpcConfigEnum;
import com.plms.rpc.util.PropertiesFileUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author bigboss
 * @Date 2021/10/29 13:12
 */
public class ConfigTest {

    @Test
    public void readConfig() throws IOException {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        System.out.println(properties.getProperty(RpcConfigEnum.ZOOKEEPER_ADDRESS.getPropertyValue()));
    }
}
