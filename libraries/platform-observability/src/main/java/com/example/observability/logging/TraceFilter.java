package com.example.observability.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
                String path = request.getRequestURI();
                // ✅ Skip internal endpoints
                if (!path.startsWith("/api")) {
                    filterChain.doFilter(request, response);
                    return;
                }  
                try {
                    String traceId = request.getHeader("X-Trace-Id");

                    if (traceId == null || traceId.isEmpty()) {
                        traceId = UUID.randomUUID().toString();
                }
                    MDC.put("traceId", traceId);
                    filterChain.doFilter(request, response);
            } finally {
                    MDC.remove("traceId");
        }
    }
}