package ru.digilabs.alkir.rahc.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import ru.digilabs.alkir.rahc.command.cli.CliPrintWriter;

@Aspect
@RequiredArgsConstructor
public class CliErrorHandlerAspect {

    @Autowired
    private CliPrintWriter printWriter;

    // catch errors in public methods of RacCliCommand subclasses
    @Around("execution(public * ru.digilabs.alkir.rahc.command.cli.RasCliCommand+.*(..))")
    public Object handleCliError(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            printWriter.printError("Cli command error, e: " + e.getMessage(), e);
            return null;
        }
    }
}
