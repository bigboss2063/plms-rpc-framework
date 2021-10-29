package com.plms.rpc.loadbalance.impl;

import com.plms.rpc.loadbalance.AbstractLoadBalance;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * 加权轮询算法 LeastActiveLoadBalance
 *
 * @Author bigboss
 * @Date 2021/10/28 22:03
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(RpcRequest rpcRequest, List<String> serviceUrlList) {
        return null;
    }
}
