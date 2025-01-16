package com.tenpo.prueba_fullstack.ratelimit;

import java.time.LocalDateTime;

public class RateLimitInfo {
    private int requestCount;
    private LocalDateTime windowStart;

    public RateLimitInfo() {
        this.requestCount = 0;
        this.windowStart = LocalDateTime.now();
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public LocalDateTime getWindowStart() {
        return windowStart;
    }

    public void resetWindow() {
        this.windowStart = LocalDateTime.now();
        this.requestCount = 1;
    }
}
