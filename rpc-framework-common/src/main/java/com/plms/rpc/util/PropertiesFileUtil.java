package com.plms.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author bigboss
 * @Date 2021/10/29 13:30
 */
@Slf4j
public class PropertiesFileUtil {
    private static Properties properties = null;

    private PropertiesFileUtil() {}

    public static Properties readPropertiesFile(String fileName) {
        try(InputStream in = PropertiesFileUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            log.error("occur exception when read properties file [{}]", fileName);
        }
        return properties;
    }
}
