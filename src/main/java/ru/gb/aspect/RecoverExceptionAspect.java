package ru.gb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.gb.annotation.RecoverException;

import java.util.Arrays;

@Aspect
@Component
public class RecoverExceptionAspect {

    @Pointcut("@annotation(ru.gb.annotation.RecoverException)")
    public void methodsAnnotatedWithRecoverException() {

    }

    @Around("methodsAnnotatedWithRecoverException()")
    public Object recoverExceptionMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?>[] noRecoverFor = extractClasses(joinPoint);
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            if (noRecoverFor != null && noRecoverFor.length != 0) {
                for (int i = 0; i < noRecoverFor.length; i++) {
                    if (noRecoverFor[i].isAssignableFrom(ex.getClass())) {
                        throw ex;
                    }
                }
            }
            Signature signature = joinPoint.getSignature();
            String returnType = ((MethodSignature) signature).getReturnType().getName();
            return switch (returnType) {
                case "byte", "short", "int" -> 0;
                case "long" -> 0L;
                case "float" -> 0.0f;
                case "double" -> 0.0d;
                case "char" -> '\u0000';
                case "boolean" -> false;
                default -> null;
            };
        }
    }

    public Class<?>[] extractClasses(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RecoverException annotation = signature.getMethod().getAnnotation(RecoverException.class);
        if (annotation != null) {
            return annotation.noRecoverFor();
        }
        return null;
    }
}
