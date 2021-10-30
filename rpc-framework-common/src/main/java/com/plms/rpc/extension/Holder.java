package com.plms.rpc.extension;

/**
 * @Author bigboss
 * @Date 2021/10/29 22:07
 */
public class Holder<T> {
    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
