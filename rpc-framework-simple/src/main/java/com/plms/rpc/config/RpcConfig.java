package com.plms.rpc.config;

import lombok.*;

/**
 * @Author bigboss
 * @Date 2021/10/27 18:59
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RpcConfig {

    private Integer weight;

    private Object service;

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
