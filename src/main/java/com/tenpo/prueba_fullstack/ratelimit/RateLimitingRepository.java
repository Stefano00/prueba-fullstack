package com.tenpo.prueba_fullstack.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class RateLimitingRepository {

	private final ConcurrentHashMap<String, RateLimitInfo> clients = new ConcurrentHashMap<>();

    public RateLimitInfo getRateLimitInfo(String clientId) {
        return clients.get(clientId);
    }

    public void setRateLimitInfo(String clientId, RateLimitInfo info) {
        clients.put(clientId, info);
    }
}
