package ru.digilabs.alkir.rahc.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.awaitility.core.ConditionTimeoutException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

@Aspect
@Component
@RequiredArgsConstructor
public class RetryableRacMethodAspect {

  private final RetryTemplate retryTemplate;

  @Around("@annotation(ru.digilabs.alkir.rahc.configuration.RetryableRacMethod)")
  public Object retryableRacMethodCall(ProceedingJoinPoint joinPoint) {

    return retryTemplate.execute(context -> {
      AtomicReference<Object> reference = new AtomicReference<>();
      var thread = Executors.defaultThreadFactory().newThread(() ->
        {
          try {
            reference.set(joinPoint.proceed());
          } catch (Throwable e) {
            throw new RuntimeException(e);
          }
        }
      );

      thread.start();

      try {
        await()
          .atMost(10_000, TimeUnit.MILLISECONDS)
          .untilAtomic(reference, notNullValue());
      } catch (ConditionTimeoutException ex) {
        thread.stop();
        throw ex;
      }

      return reference.get();
    });

  }
}
