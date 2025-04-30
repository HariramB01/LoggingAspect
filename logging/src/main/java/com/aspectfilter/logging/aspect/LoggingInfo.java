package com.aspectfilter.logging.aspect;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingInfo {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInfo.class);

    @Around("@annotation(LoggingAspect)")
    public Object logDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        String trackingId = MDC.get("trackingId");
        if(trackingId==null){
            trackingId = UUID.randomUUID().toString();
            MDC.put("trackingId", trackingId);
        }
        String signature = joinPoint.getSignature().toShortString();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String arguments = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof HttpServletRequest)
                .map(arg -> (HttpServletRequest) arg).collect(Collectors.toList()).get(0).getQueryString();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String httpMethod = null;
        String requestURI = null;
        if(attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            httpMethod = request.getMethod();
            requestURI = request.getRequestURI();
        }
        logger.info("Tracking ID: {} - Entering method: {} - Arguments: {} - HTTP Method: {} - Request URI: {}", trackingId, signature, arguments, httpMethod, requestURI);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Tracking ID: {} - Error in method: {} - Arguments: {} - HTTP Method: {} - Request URI: {}", trackingId, signature, arguments, httpMethod, requestURI, throwable);
            throw throwable;
        } finally {
            logger.info("Tracking ID: {} - Exiting method: {} - Arguments: {} - HTTP Method: {} - Request URI: {}", trackingId, signature, arguments, httpMethod, requestURI);
            MDC.clear();
        }
        return result;
    }

}
