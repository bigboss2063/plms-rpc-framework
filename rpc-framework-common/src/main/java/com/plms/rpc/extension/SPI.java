package com.plms.rpc.extension;

import java.lang.annotation.*;

/**
 * @Author bigboss
 * @Date 2021/10/26 15:21
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    String value() default "";
}
