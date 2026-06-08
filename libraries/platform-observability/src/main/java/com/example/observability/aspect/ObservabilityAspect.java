package com.example.observability.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.observability.annotation.Track;
import com.example.observability.core.ObservabilityService;

@Aspect
@Component
public class ObservabilityAspect {

    private final ObservabilityService observabilityService;

    public ObservabilityAspect(ObservabilityService observabilityService) {
        this.observabilityService = observabilityService;
    }

    @Around("@annotation(track)")
    public Object around(ProceedingJoinPoint joinPoint, Track track) throws Throwable {

        String operation = track.value();

        return observabilityService.track(operation, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}