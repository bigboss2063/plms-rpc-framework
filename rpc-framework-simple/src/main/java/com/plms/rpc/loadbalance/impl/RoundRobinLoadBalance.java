package com.plms.rpc.loadbalance.impl;

import com.plms.rpc.loadbalance.AbstractLoadBalance;
import com.plms.rpc.remoting.dto.RpcRequest;
import com.plms.rpc.util.AtomicPositiveInteger;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权轮询算法-RoundRobinLoadBalance
 * 1、采用一个全局的map保存每个服务和服务的请求次数
 * 2、计算总的权重weightSum，并提取每个服务提供者的权重放进局部serviceToMap中
 * 3、用当前轮询序号与服务提供者总权重取模，余数为
 *
 * @Author bigboss
 * @Date 2021/10/28 22:03
 */
@Slf4j
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final ConcurrentMap<String, AtomicInteger> sequences = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(RpcRequest rpcRequest, List<String> serviceUrlList) {
        String key = rpcRequest.getServiceName();
        int length = serviceUrlList.size();
        int maxWeight = 0;
        int minWeight = Integer.MAX_VALUE;
        final LinkedHashMap<String, AtomicInteger> serviceToWeightMap = new LinkedHashMap<>();
        int weightSum = 0;
        for (String s : serviceUrlList) {
            int weight = getWeight(s);
            maxWeight = Math.max(maxWeight, weight);
            minWeight = Math.min(minWeight, weight);
            if (weight > 0) {
                serviceToWeightMap.put(s, new AtomicInteger(weight));
                weightSum += weight;
            }
        }
        AtomicInteger sequence = sequences.get(key);
        if (sequence == null) {
            sequences.putIfAbsent(key, new AtomicInteger(0));
            sequence = sequences.get(key);
        }
        int currentSequence = sequence.getAndIncrement();
        if (maxWeight > 0 && minWeight < maxWeight) {
            int mod = currentSequence % weightSum;
            for (int i = 0; i < maxWeight; i++) {
                for (Map.Entry<String, AtomicInteger> each : serviceToWeightMap.entrySet()) {
                    final String serviceUrl = each.getKey();
                    final AtomicInteger weight = each.getValue();
                    if (mod == 0 && weight.intValue() > 0) {
                        return serviceUrl;
                    }
                    if (weight.intValue() > 0) {
                        weight.decrementAndGet();
                        mod--;
                    }
                }
            }
        }
        return serviceUrlList.get(currentSequence % length);
    }

    int getWeight(String serviceUrl) {
        return Integer.parseInt(serviceUrl.split("#")[1]);
    }
}
