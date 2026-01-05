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
 * 日志切面类（AOP）
 */
@Slf4j
@Aspect // AOP切面类
@Component
public class LoggingAspect {

    /**
     * 定义切入点（Pointcut）：匹配所有标注了@Loggable自定义注解的方法
     */
    @Pointcut("@annotation(org.code.privateclinic.annotation.Loggable)")
    public void loggableMethods() {
        // 「切入点标记」
    }

    /**
     * 环绕通知（Around）：在目标方法执行前后执行自定义逻辑
     * 可控制目标方法是否执行、修改返回值、处理异常
     */
    @Around("loggableMethods()") // 指定该通知作用于上面定义的loggableMethods切入点
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录方法执行开始时间（用于计算总耗时）
        long startTime = System.currentTimeMillis();

        // 获取目标方法的签名信息（强转为MethodSignature，因为切入点是方法）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod(); // 拿到目标方法的反射对象

        // 获取目标方法上的@Loggable注解（读取注解中的自定义描述）
        Loggable loggable = method.getAnnotation(Loggable.class);

        // 拼接日志展示的基础信息
        // 获取目标方法所属类的简单类名（如MedicalCaseController）
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // 优先使用注解的value作为日志描述，若为空则使用方法名
        String description = loggable.value().isEmpty() ? method.getName() : loggable.value();

        // 打印「方法开始执行」日志
        log.info("开始执行: {}.{}()", className, description);

        try {
            // 执行目标方法（核心步骤：调用被注解标记的业务方法）
            Object result = joinPoint.proceed();

            // 计算方法执行耗时（结束时间 - 开始时间）
            long duration = System.currentTimeMillis() - startTime;

            // 根据返回值类型，打印不同格式的「执行成功」日志
            if (result instanceof Collection) {
                // 若返回值是集合（List/Set等），额外打印集合大小
                int size = ((Collection<?>) result).size();
                log.info("执行成功: {}，返回 {} 条记录，耗时 {} ms", description, size, duration);
            } else if (result != null) {
                // 若返回值非空且非集合，仅打印耗时
                log.info("执行成功: {}，耗时 {} ms", description, duration);
            } else {
                // 若返回值为null，打印警告级日志（区分正常空返回和异常）
                log.warn("执行完成: {}，返回 null，耗时 {} ms", description, duration);
            }

            // 返回目标方法的执行结果（保证业务逻辑的返回值不受切面影响）
            return result;
        } catch (Exception e) {
            // 捕获目标方法抛出的异常，记录「执行异常」日志
            long duration = System.currentTimeMillis() - startTime;
            // 打印error级日志，包含描述、错误信息、耗时，最后传入e打印完整堆栈（便于排查问题）
            log.error("执行异常: {}，错误信息: {}，耗时 {} ms", description, e.getMessage(), duration, e);
            // 重新抛出异常（切面仅记录日志，不处理异常，由业务层自行处理）
            throw e;
        }
    }
}