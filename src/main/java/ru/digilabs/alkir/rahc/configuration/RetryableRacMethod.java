package ru.digilabs.alkir.rahc.configuration;

import com._1c.v8.ibis.V8Exception;
import com._1c.v8.ibis.admin.AgentAdminException;
import org.springframework.retry.annotation.Retryable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Retryable(value = {V8Exception.class, AgentAdminException.class})
public @interface RetryableRacMethod {
}
