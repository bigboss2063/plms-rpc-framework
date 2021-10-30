package com.plms.rpc.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author bigboss
 * @Date 2021/10/27 8:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 接口名
     */
    private String serviceName;
    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法返回类型
     */
    private Class<?> returnType;

    /**
     * 参数类型数组
     */
    private Class[] parameterTypes;

    /**
     * 参数数值数组
     */
    private Object[] parameterValues;

    /**
     * 实现类分组
     */
    private String group;

    /**
     * 版本号
     */
    private String version;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
