package com.bankai.authservice.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut to match all classes annotated with @Service or @RestController
    @Pointcut("@within(org.springframework.stereotype.Service) || @within(org.springframework.web.bind.annotation.RestController)")
    public void beanAnnotatedWithServiceOrRestController() {
        // Nobody needed – it's a pointcut definition
    }

    // Pointcut to match any method in your base package
    @Pointcut("within(com.bankai.authservice..*)")
    public void applicationPackagePointcut() {
        // Nobody needed – it's a pointcut definition
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ Entering: {} with args: {}", method, args);

        try {
            Object result = joinPoint.proceed();
            log.info("✅ Exiting: {} with result: {}", method, result);
            return result;
        } catch (Throwable e) {
            log.error("❌ Exception in: {} - {}", method, e.getMessage(), e);
            throw e;
        }
    }
} 