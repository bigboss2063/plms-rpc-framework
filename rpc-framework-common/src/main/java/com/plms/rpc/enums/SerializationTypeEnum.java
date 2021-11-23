package com.plms.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author bigboss
 * @Date 2021/11/23 10:55
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {

    /**
     * kyro
     */
    KYRO((byte) 0x01 ,"kyro");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum value : SerializationTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return null;
    }
}
