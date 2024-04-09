package ru.gb.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.gb.CustomClass;

@Slf4j
@Component
@RequiredArgsConstructor
public class AspectRunner {
    private final CustomClass customClass;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws Throwable {
        customClass.method1();
        customClass.method2();
        log.info("Throwable method 1 output: {}", customClass.throwableMethod1());

        try {
            log.info("Throwable method 2 output: {}", customClass.throwableMethod2());
        } catch (Exception e) {
            log.info("Method 2 output: " + e.getMessage());
        }
    }
}
