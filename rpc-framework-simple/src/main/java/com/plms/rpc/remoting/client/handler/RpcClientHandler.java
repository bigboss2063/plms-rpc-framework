package com.plms.rpc.remoting.client.handler;

import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.exception.RpcException;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.remoting.client.NettyRpcClient;
import com.plms.rpc.remoting.dto.RpcMessage;
import com.plms.rpc.remoting.dto.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bigboss
 * @Date 2021/10/27 14:00
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final NettyRpcClient NETTY_RPC_CLIENT;
    public static final Map<String, Promise<Object>> UN_PROCESSED_TASK = new ConcurrentHashMap<>();

    public RpcClientHandler() {
        this.NETTY_RPC_CLIENT = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage){
        log.info("{}", rpcMessage);
        if (rpcMessage.getMessageType() != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            RpcResponse<Object> rpcResponse = (RpcResponse<Object>) rpcMessage.getData();
            Promise<Object> promise = UN_PROCESSED_TASK.remove(rpcResponse.getRequestId());
            if (promise != null) {
                if (rpcResponse.getCode() == 200) {
                    promise.setSuccess(rpcResponse.getData());
                } else if (rpcResponse.getCode() == 400) {
                    promise.setFailure(new RpcException("remote call is failed!"));
                }
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                log.info("writer idle event has been trigger");
                Channel channel = NETTY_RPC_CLIENT.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder()
                        .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                        .codec((byte) 1)
                        .compress((byte) 1)
                        .data(RpcConstants.PING)
                        .build();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("connection has been cut!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client exception was caught");
        ctx.close();
        cause.printStackTrace();
    }
}
