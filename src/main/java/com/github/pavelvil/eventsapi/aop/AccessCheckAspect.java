package com.github.pavelvil.eventsapi.aop;

import com.github.pavelvil.eventsapi.exception.AccessDeniedException;
import com.github.pavelvil.eventsapi.service.checker.AccessCheckerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.Map;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class AccessCheckAspect {

    private final Map<AccessCheckType, AccessCheckerService> accessCheckServiceMap;

    @Before("@annotation(accessible)")
    public void check(JoinPoint joinPoint, Accessible accessible) {
        if (accessible == null) {
            throw new IllegalArgumentException("Accessible is null!");
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new IllegalArgumentException("RequestAttributes not present!");
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        AccessCheckerService checkerService = accessCheckServiceMap.get(accessible.checkBy());

        if (checkerService == null) {
            throw new IllegalArgumentException(
                    MessageFormat.format("AccessCheckerService for type {0} not found", accessible.checkBy())
            );
        }

        if (!checkerService.check(request, accessible)) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            log.error("Access denied for action: " + signature.getMethod());
            throw new AccessDeniedException("Access denied for this action!");
        }
    }
}
