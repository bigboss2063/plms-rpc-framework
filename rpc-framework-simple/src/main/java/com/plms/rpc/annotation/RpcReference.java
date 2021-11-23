package com.plms.rpc.annotation;

import java.lang.annotation.*;

/**
 * @Author bigboss
 * @Date 2021/11/23 11:02
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    String version() default "";

    String group() default "";
}
