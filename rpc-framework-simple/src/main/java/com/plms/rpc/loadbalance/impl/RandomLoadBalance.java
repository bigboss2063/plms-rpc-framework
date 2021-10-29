package com.plms.rpc.loadbalance.impl;

import com.plms.rpc.loadbalance.AbstractLoadBalance;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * 随机权重-RandomLoadBalance
 * 1、对所有的invoker列表权重求和，和为totalWeight。
 * 2、在0-totalWeight中随机取一个整数offset。
 * 3、offset依次减去每一个的invoker的权重，offset小于0时终止条件
 *
 * @Author bigboss
 * @Date 2021/10/28 20:48
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    private Random random;

    @Override
    protected String doSelect(RpcRequest rpcRequest, List<String> serviceUrlList) {
        random = new Random();
        int length = serviceUrlList.size();
        int totalWeight = 0;
        boolean sameWeight = true;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(serviceUrlList.get(i));
            totalWeight += weight;
            if (sameWeight && i>0 && weight != getWeight(serviceUrlList.get(i-1))) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            int offset = random.nextInt(totalWeight);
            for (String url: serviceUrlList) {
                offset -= getWeight(url);
                if (offset < 0) {
                    return url.split("#")[0];
                }
            }
        }
        return serviceUrlList.get(random.nextInt(length)).split("#")[0];
    }

    int getWeight(String serviceUrl) {
        return Integer.parseInt(serviceUrl.split("#")[1]);
    }
}
