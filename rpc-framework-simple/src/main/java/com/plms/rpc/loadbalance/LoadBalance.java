package com.plms.rpc.loadbalance;

import com.plms.rpc.extension.SPI;
import com.plms.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @Author bigboss
 * @Date 2021/10/28 20:44
 */
@SPI
public interface LoadBalance {
    String serverLoadBalance(RpcRequest rpcRequest, List<String> serviceUrlList);
}
