package com.plms.rpc.remoting.dto;

import com.plms.rpc.serialize.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author bigboss
 * @Date 2021/10/27 8:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    /**
     * 所相应的请求id
     */
    private String requestId;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应码
     */
    private Integer code;
}
