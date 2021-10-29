package com.plms.rpc.exception;

/**
 * @Author bigboss
 * @Date 2021/10/27 9:52
 */
public class RpcException extends RuntimeException{

    public RpcException(String message) {
        super(message);
    }
}
