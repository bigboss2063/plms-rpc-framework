package com.plms.rpc.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author bigboss
 * @Date 2021/11/23 11:04
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {

    String version() default "";

    String group() default "";

    int weight() default 0;
}
