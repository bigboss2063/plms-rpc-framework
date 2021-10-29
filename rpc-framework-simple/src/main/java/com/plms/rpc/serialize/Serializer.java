package com.plms.rpc.serialize;

/**
 * @Author bigboss
 * @Date 2021/10/27 10:07
 */
public interface Serializer {

    byte[] serializer(Object obj);

    <T> T deserializer(byte[] bytes, Class<T> clazz);
}
