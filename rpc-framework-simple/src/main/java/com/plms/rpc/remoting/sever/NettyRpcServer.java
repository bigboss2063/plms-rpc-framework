package com.plms.rpc.remoting.sever;

import com.plms.rpc.config.RpcConfig;
import com.plms.rpc.factory.SingletonFactory;
import com.plms.rpc.provider.ServiceProvider;
import com.plms.rpc.provider.impl.ZkServiceProviderImpl;
import com.plms.rpc.remoting.codec.RpcMessageDecoder;
import com.plms.rpc.remoting.codec.RpcMessageEncoder;
import com.plms.rpc.remoting.sever.handler.RpcServerHandler;
import com.plms.rpc.remoting.sever.hooker.ServiceShutDownHooker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author bigboss
 * @Date 2021/10/27 13:22
 */
@Slf4j
@Component(value = "nettyRpcServer")
public class NettyRpcServer {

    public static Integer PORT;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup workers;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    public void registerService(RpcConfig rpcConfig) {
        serviceProvider.publishService(rpcConfig);
    }

    public void start() {
        try {
            ServiceShutDownHooker.getServiceShutDownHooker().clearAllServices();
            boss = new NioEventLoopGroup(1);
            workers = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, workers)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(10, 0, 0));
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(9999).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            boss.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }
}
