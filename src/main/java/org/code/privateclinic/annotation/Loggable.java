package org.code.privateclinic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解，用于标记需要记录日志的方法
 */
@Target(ElementType.METHOD) //该注解只能用在方法上
@Retention(RetentionPolicy.RUNTIME) //注解会保留到程序运行时
public @interface Loggable {
    /**
     * 操作描述
     */
    String value() default "";
}

