package com.plms.rpc.serialize;

import com.plms.rpc.remoting.dto.RpcRequest;
import com.plms.rpc.serialize.kryo.KryoSerializer;
import org.junit.Test;

import java.util.Random;

/**
 * @Author bigboss
 * @Date 2021/10/27 10:22
 */
public class SerializeTest {

    @Test
    public void kryoSerializeTest() {
        RpcRequest target = RpcRequest.builder()
                .serviceName("bigboss")
                .methodName("snake")
                .returnType(String.class)
                .requestId("1")
                .parameterTypes(new Class<?>[]{String.class, Integer.class})
                .parameterValues(new Object[]{"promise", 1})
                .build();
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serializer(target);
        RpcRequest rpcRequest = serializer.deserializer(bytes, RpcRequest.class);
        System.out.println(rpcRequest.toString());
    }

    @Test
    public void randomTest() {
        Random random = new Random();
        System.out.println(random.nextInt(256));
    }
}
