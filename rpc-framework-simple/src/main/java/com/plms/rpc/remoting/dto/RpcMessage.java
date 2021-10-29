package com.plms.rpc.remoting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author bigboss
 * @Date 2021/10/27 9:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcMessage {
    //rpc message type
    private byte messageType;
    //serialization type
    private byte codec;
    //compress type
    private byte compress;
    //request id
    private int requestId;
    //request data
    private Object data;
}
