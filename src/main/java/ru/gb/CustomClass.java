package ru.gb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.gb.annotation.RecoverException;
import ru.gb.annotation.Timer;

@Slf4j
@Component
public class CustomClass {

    @Timer
    public void method1() throws InterruptedException {
       Thread.sleep(500);
    }

    @Timer
    public void method2() throws InterruptedException {
        Thread.sleep(300);
    }

    @RecoverException
    public int throwableMethod1() {
        return 10 / 0;
    }

    @RecoverException(noRecoverFor = {RuntimeException.class})
    public int throwableMethod2() {
        throw new IllegalArgumentException("Throwable Method 2 exception!");
    }
}
