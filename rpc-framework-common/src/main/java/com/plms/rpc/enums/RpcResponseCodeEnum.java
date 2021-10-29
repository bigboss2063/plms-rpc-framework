package com.plms.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author bigboss
 * @Date 2021/10/27 14:30
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCodeEnum {

    SUCCESS(200, "The remote call is successful"),
    FAIL(400, "The remote call is fail");
    private final int code;

    private final String message;
}
