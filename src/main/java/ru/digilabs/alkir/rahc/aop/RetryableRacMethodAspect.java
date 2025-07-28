package ru.digilabs.alkir.rahc.aop;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.awaitility.core.ConditionTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

@Aspect
@RequiredArgsConstructor
public class RetryableRacMethodAspect {

    @Autowired
    private RetryTemplate racRetryTemplate;
    @Autowired
    private RetrayableRacMethodExceptionHandler retrayableRacMethodExceptionHandler;

    @Around("@annotation(ru.digilabs.alkir.rahc.configuration.RetryableRacMethod)")
    @SneakyThrows
    public Object retryableRacMethodCall(ProceedingJoinPoint joinPoint) {

        // TODO: почему у аспекта два инстанса?
        return racRetryTemplate.execute(new RetryCallback<Object, Throwable>() {
            @Override
            public String getLabel() {
                return "retryableRacMethodCall " + joinPoint.getSignature().getName();
            }

            @Override
            public Object doWithRetry(RetryContext context) throws Throwable {
                AtomicReference<Object> reference = new AtomicReference<>();
                AtomicReference<Throwable> throwable = new AtomicReference<>();
                var thread = Executors.defaultThreadFactory().newThread(() ->
                    {
                        try {
                            reference.set(joinPoint.proceed());
                        } catch (Throwable e) {
                            reference.set(new Object());
                            throwable.set(e);
                            retrayableRacMethodExceptionHandler.handle(e);
                        }
                    }
                );

                thread.start();

                try {
                    await()
                        .atMost(10_000, TimeUnit.MILLISECONDS)

                        .untilAtomic(reference, notNullValue());
                } catch (ConditionTimeoutException ex) {
                    thread.interrupt();
                    throw ex;
                }

                if (throwable.get() != null) {
                    throw throwable.get();
                }

                return reference.get();
            }
        });

    }
}
