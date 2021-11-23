package com.plms.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author bigboss
 * @Date 2021/11/23 15:48
 */
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {

    /**
     * gzip压缩方式
     */
    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum value : CompressTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return null;
    }
}
