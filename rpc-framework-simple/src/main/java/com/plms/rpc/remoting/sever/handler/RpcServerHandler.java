package com.plms.rpc.remoting.sever.handler;

import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.enums.RpcResponseCodeEnum;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.provider.ServiceProvider;
import com.plms.rpc.provider.impl.ZkServiceProviderImpl;
import com.plms.rpc.remoting.dto.RpcMessage;
import com.plms.rpc.remoting.dto.RpcRequest;
import com.plms.rpc.remoting.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author bigboss
 * @Date 2021/10/27 20:01
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcRequestMessage) throws Exception {
        ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        RpcMessage rpcResponseMessage = RpcMessage.builder()
                .codec((byte) 1)
                .compress((byte) 1)
                .build();
        if (rpcRequestMessage.getMessageType() == RpcConstants.REQUEST_TYPE) {
            rpcResponseMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
            RpcRequest rpcRequest = (RpcRequest) rpcRequestMessage.getData();
            Object service = serviceProvider.getService(rpcRequest.getServiceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(service, rpcRequest.getParameterValues());
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                RpcResponse<Object> rpcResponse = RpcResponse.builder()
                        .code(RpcResponseCodeEnum.SUCCESS.getCode())
                        .message("remote call is successful!")
                        .requestId(rpcRequest.getRequestId())
                        .data(result)
                        .build();
                rpcResponseMessage.setData(rpcResponse);
            } else {
                RpcResponse<Object> rpcResponse = RpcResponse.builder()
                        .code(RpcResponseCodeEnum.FAIL.getCode())
                        .message("remote call is failed!")
                        .requestId(rpcRequest.getRequestId())
                        .build();
                rpcResponseMessage.setData(rpcResponse);
            }
        } else if(rpcRequestMessage.getMessageType() == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcResponseMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
            rpcResponseMessage.setData(RpcConstants.PONG);
        }
        ctx.writeAndFlush(rpcResponseMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                ctx.channel().close();
                log.info("More than 30 seconds without receiving client messages, automatically disconnect with client");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("connection has been cut!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("server exception was caught!");
        ctx.close();
        cause.printStackTrace();
    }
}
