package com.plms.rpc.serialize;

import com.plms.rpc.extension.SPI;

/**
 * @Author bigboss
 * @Date 2021/10/27 10:07
 */
@SPI(value = "kyro")
public interface Serializer {

    byte[] serializer(Object obj);

    <T> T deserializer(byte[] bytes, Class<T> clazz);
}
