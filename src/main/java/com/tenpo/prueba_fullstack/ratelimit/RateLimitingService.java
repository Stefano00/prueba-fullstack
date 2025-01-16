package com.tenpo.prueba_fullstack.ratelimit;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenpo.prueba_fullstack.exception.RateLimitExceededException;

@Service
public class RateLimitingService {
    
    private static final int MAX_REQUESTS_PER_MINUTE = 3;
    private static final long WINDOW_DURATION_SECONDS = 60; // 1 minuto

    @Autowired
    private RateLimitingRepository rateLimitingRepository;

    public void validateRequest(String clientId) {
        RateLimitInfo info = rateLimitingRepository.getRateLimitInfo(clientId);

        LocalDateTime now = LocalDateTime.now();
        
        if (info == null) {
            info = new RateLimitInfo();
            info.incrementRequestCount();
            rateLimitingRepository.setRateLimitInfo(clientId, info);
            return;
        }

        Duration elapsed = Duration.between(info.getWindowStart(), now);
        if (elapsed.getSeconds() >= WINDOW_DURATION_SECONDS) {
            info.resetWindow();
        } else {
            if (info.getRequestCount() >= MAX_REQUESTS_PER_MINUTE) {
                throw new RateLimitExceededException("Se excedió el límite de " + MAX_REQUESTS_PER_MINUTE + " solicitudes por minuto.");
            }
            info.incrementRequestCount();
        }
        
        rateLimitingRepository.setRateLimitInfo(clientId, info);
    }
}
