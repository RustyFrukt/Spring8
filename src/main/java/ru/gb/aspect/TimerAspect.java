package ru.gb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimerAspect {

    @Pointcut("within(@ru.gb.annotation.Timer *)")
    public void beansAnnotatedWithTimer() {

    }

    @Pointcut("@annotation(ru.gb.annotation.Timer)")
    public void methodsAnnotatedWithTimer() {

    }

    @Around("beansAnnotatedWithTimer() || methodsAnnotatedWithTimer()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info(joinPoint.getTarget().getClass().getName() + " - " + joinPoint.getSignature().getName() + " #" + elapsedTime);
        return result;
    }
}
