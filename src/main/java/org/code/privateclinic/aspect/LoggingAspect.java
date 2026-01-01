package org.code.privateclinic.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.code.privateclinic.annotation.Loggable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 日志切面，统一处理日志记录
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(org.code.privateclinic.annotation.Loggable)")
    public void loggableMethods() {
    }

    @Around("loggableMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggable = method.getAnnotation(Loggable.class);
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String description = loggable.value().isEmpty() ? method.getName() : loggable.value();
        
        log.info("开始执行: {}.{}()", className, description);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            if (result instanceof Collection) {
                int size = ((Collection<?>) result).size();
                log.info("执行成功: {}，返回 {} 条记录，耗时 {} ms", description, size, duration);
            } else if (result != null) {
                log.info("执行成功: {}，耗时 {} ms", description, duration);
            } else {
                log.warn("执行完成: {}，返回 null，耗时 {} ms", description, duration);
            }

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("执行异常: {}，错误信息: {}，耗时 {} ms", description, e.getMessage(), duration, e);
            throw e;
        }
    }
}

