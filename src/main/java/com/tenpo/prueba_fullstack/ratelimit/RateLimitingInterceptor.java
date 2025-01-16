package com.tenpo.prueba_fullstack.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS = 3;
    private static final long TIME_WINDOW_MS = 60000; // 1 minuto
    private static final int HTTP_STATUS_TOO_MANY_REQUESTS = 429; // Código de estado para Too Many Requests

    private final ConcurrentHashMap<String, RequestCounter> clientRequestMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if ("PUT".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if ("DELETE".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        clientRequestMap.compute(clientIp, (key, counter) -> {
            if (counter == null || currentTime - counter.startTime > TIME_WINDOW_MS) {
                return new RequestCounter(1, currentTime);
            }
            counter.increment();
            return counter;
        });

        RequestCounter counter = clientRequestMap.get(clientIp);

        if (counter.requests > MAX_REQUESTS) {
            response.setStatus(HTTP_STATUS_TOO_MANY_REQUESTS);
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return false; // Bloquea la solicitud
        }

        return true; // Permite la solicitud
    }

    private static class RequestCounter {
        private int requests;
        private long startTime;

        public RequestCounter(int requests, long startTime) {
            this.requests = requests;
            this.startTime = startTime;
        }

        public void increment() {
            requests++;
        }
    }
}
