package com.plms.rpc.codec;

import com.plms.rpc.remoting.codec.RpcMessageDecoder;
import com.plms.rpc.remoting.codec.RpcMessageEncoder;
import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.remoting.dto.RpcMessage;
import com.plms.rpc.remoting.dto.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

/**
 * @Author bigboss
 * @Date 2021/10/27 11:34
 */
public class CodecTest {

    @Test
    public void codecMethodTest() throws Exception {
        RpcRequest target = RpcRequest.builder()
                .methodName("snake")
                .serviceName("bigboss")
                .returnType(String.class)
                .requestId("1")
                .parameterTypes(new Class<?>[]{String.class, Integer.class})
                .parameterValues(new Object[]{"promise", 1})
                .build();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(RpcConstants.REQUEST_TYPE)
                .compress((byte) 1)
                .codec((byte) 1)
                .data(target)
                .build();
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new RpcMessageDecoder(),
                new RpcMessageEncoder()
        );
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new RpcMessageEncoder().encode(null, rpcMessage, byteBuf);
        channel.writeInbound(byteBuf);
    }
}
