package com.plms.rpc.remoting.client;

import com.plms.rpc.enums.CompressTypeEnum;
import com.plms.rpc.enums.SerializationTypeEnum;
import com.plms.rpc.exception.RpcException;
import com.plms.rpc.extension.ExtensionLoader;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.register.ServiceDiscovery;
import com.plms.rpc.register.zk.ZkServiceDiscoveryImpl;
import com.plms.rpc.remoting.client.handler.RpcClientHandler;
import com.plms.rpc.remoting.client.provider.ChannelProvider;
import com.plms.rpc.remoting.codec.RpcMessageDecoder;
import com.plms.rpc.remoting.codec.RpcMessageEncoder;
import com.plms.rpc.constant.RpcConstants;
import com.plms.rpc.remoting.dto.RpcMessage;
import com.plms.rpc.remoting.dto.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @Author bigboss
 * @Date 2021/10/27 13:22
 */
@Slf4j
public class NettyRpcClient {

    private Bootstrap bootstrap;
    private NioEventLoopGroup eventLoopGroup;
    private ServiceDiscovery serviceDiscovery;
    private ChannelProvider channelProvider;
    public NettyRpcClient() {
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zkDiscovery");
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 5 ,0));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new RpcClientHandler());
                    }
                });
    }

    @SneakyThrows
    private Channel doConnect(InetSocketAddress address) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(address).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                throw new RuntimeException("connecting with sever failed");
            }
        });
        return completableFuture.get();
    }

    public Channel getChannel(InetSocketAddress address) {
        Channel channel = channelProvider.get(address);
        if (channel == null) {
            channel = doConnect(address);
            channelProvider.set(address, channel);
        }
        return channel;
    }

    public Object sendRpcRequest(RpcRequest rpcRequest) throws InterruptedException {
        InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryService(rpcRequest);
        RpcMessage rpcMessage = RpcMessage
                .builder()
                .messageType(RpcConstants.REQUEST_TYPE)
                .compress(CompressTypeEnum.GZIP.getCode())
                .codec(SerializationTypeEnum.KYRO.getCode())
                .data(rpcRequest)
                .build();
        Channel channel = getChannel(inetSocketAddress);
        channel.writeAndFlush(rpcMessage);
        DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
        RpcClientHandler.UN_PROCESSED_TASK.put(rpcRequest.getRequestId(), promise);
        promise.await();
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            throw new RpcException("remote call is failed!");
        }
    }
}
