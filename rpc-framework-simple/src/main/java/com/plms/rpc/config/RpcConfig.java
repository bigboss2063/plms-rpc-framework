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

    private String group = "";

    private String version = "";

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
