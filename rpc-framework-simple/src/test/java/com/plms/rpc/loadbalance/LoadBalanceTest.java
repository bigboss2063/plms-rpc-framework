package com.plms.rpc.loadbalance;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author bigboss
 * @Date 2021/10/29 18:00
 */
public class LoadBalanceTest {

    @Test
    public void roundRobinLoadBalanceTest() {
        //存储调用的方法和总的调用次数的map
        final ConcurrentMap<String, AtomicInteger> sequences = new ConcurrentHashMap<String, AtomicInteger>();
        //统计invoker 被调用次数map
        final ConcurrentMap<String, AtomicInteger> result = new ConcurrentHashMap<String, AtomicInteger>();
        //模拟方法三个invoker 分别为 a,b,c
        List<String> invokes = new ArrayList<String>(3);
        invokes.add("a");
        invokes.add("b");
        invokes.add("c");
        //存储invoker和权重的对应map
        final LinkedHashMap<String, AtomicInteger> invokerToWeightMap = new LinkedHashMap<String, AtomicInteger>();
        for (int i = 0; i < 18; i++) {
            //每次调用都把模拟的权重重新放入
            invokerToWeightMap.put("a", new AtomicInteger(3));
            invokerToWeightMap.put("b", new AtomicInteger(6));
            invokerToWeightMap.put("c", new AtomicInteger(9));
            select(invokes, invokerToWeightMap, sequences, result);
        }
        //打印调用结果统计
        for (Map.Entry<String, AtomicInteger> r : result.entrySet()) {
            System.out.println(r.getKey() + "被调用:" + r.getValue() + "次");
        }
    }

    private static void select(List<String> invokes,
                               LinkedHashMap<String, AtomicInteger> invokerToWeightMap,
                               ConcurrentMap<String, AtomicInteger> sequences,
                               ConcurrentMap<String, AtomicInteger> result) {
        //假设调用servcie.hello方法
        AtomicInteger sequence = sequences.get("service.hello");
        if (sequence == null) {
            //默认调用次数为0
            sequences.putIfAbsent("servcie.hello", new AtomicInteger(0));
            sequence = sequences.get("servcie.hello");
        }
        //调用次数+1
        int currentSequence = sequence.getAndIncrement();
        System.out.print("currentSequence:" + currentSequence);
        int maxWeight = 9;//最大权重
        int minWeight = 3;//最小权重
        int weightSum = 18;//总权重
        if (maxWeight > 0 && minWeight < maxWeight) { // 走权重不一样逻辑。
            int mod = currentSequence % weightSum;
            System.out.print(" mod:" + mod);
            for (int i = 0; i < maxWeight; i++) {
                for (Map.Entry<String, AtomicInteger> each : invokerToWeightMap.entrySet()) {
                    final String k = each.getKey();
                    final AtomicInteger v = each.getValue();
                    if (mod == 0 && v.intValue() > 0) {
                        System.out.println(" selected:" + k);
                        AtomicInteger count = result.get(k);
                        if (count == null) {
                            result.putIfAbsent(k, new AtomicInteger(1));
                        } else {
                            count.incrementAndGet();
                        }
                        return;
                    }
                    if (v.intValue() > 0) {
                        v.decrementAndGet();
                        mod--;
                    }
                }
            }
        }

    }
}
