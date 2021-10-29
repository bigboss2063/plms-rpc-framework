package com.plms.rpc.loadbalance;

import com.plms.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @Author bigboss
 * @Date 2021/10/28 20:47
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String serverLoadBalance(RpcRequest rpcRequest, List<String> serviceUrlList) {
        if (serviceUrlList == null || serviceUrlList.isEmpty()) {
            return null;
        }
        if (serviceUrlList.size() == 1) {
            return serviceUrlList.get(0);
        }
        return doSelect(rpcRequest, serviceUrlList);
    }

    protected abstract String doSelect(RpcRequest rpcRequest, List<String> serviceUrlList);
}
