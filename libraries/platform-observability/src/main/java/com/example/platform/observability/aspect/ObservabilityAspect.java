package com.example.platform.observability.aspect;

import com.example.platform.observability.annotation.Track;
import com.example.platform.observability.core.ObservabilityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

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