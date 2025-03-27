package com.angelvb.kafka_listener.util;

import com.angelvb.kafka_listener.model.DemoRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Aspect
@Component
public class DemoClientAspect {

  @Around("execution(* com.angelvb.kafka_listener.client.DemoClient.notifyUser(..))")
  public Mono<?> logDemoClientRequest(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    DemoRequestBody requestBody = (DemoRequestBody) args[0];

    log.info("Request to notifyUser: {}", requestBody);

    Mono<?> responseMono = (Mono<?>) joinPoint.proceed();

    return responseMono.doOnNext(response -> log.info("Response from notifyUser: {}", response))
        .doOnError(error -> log.error("Error calling notifyUser: {}", error.getMessage()));
  }
}
