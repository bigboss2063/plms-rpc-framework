package com.plms.rpc.annotation;

import com.plms.rpc.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author bigboss
 * @Date 2021/11/23 11:21
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(CustomScannerRegistrar.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcScan {

    String [] basePackage();
}
