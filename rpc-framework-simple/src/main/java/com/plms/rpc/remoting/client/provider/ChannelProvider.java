package com.plms.rpc.remoting.client.provider;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bigboss
 * @Date 2021/10/27 13:47
 */
public class ChannelProvider {

    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress address) {
        String key = address.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if(channel !=null && channel.isActive()) {
                return channel;
            }
        }
        return null;
    }

    public void set(InetSocketAddress address, Channel channel) {
        String key = address.toString();
        channelMap.put(key, channel);
    }
}
