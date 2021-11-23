package com.plms.rpc.compress;

import com.plms.rpc.extension.SPI;

/**
 * @Author bigboss
 * @Date 2021/11/23 15:53
 */
@SPI
public interface Compress {

    /**
     * 压缩
     * @param bytes
     * @return
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压
     * @param bytes
     * @return
     */
    byte[] decompress(byte[] bytes);
}
