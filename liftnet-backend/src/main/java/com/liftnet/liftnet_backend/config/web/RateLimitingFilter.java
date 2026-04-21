package com.liftnet.liftnet_backend.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liftnet.liftnet_backend.common.response.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // Máx. peticiones permitidas
    private static final int MAX_REQUESTS = 100;

    // Ventana de tiempo (1 minuto)
    private static final long WINDOW_MS = 60_000;

    // IP -> contador
    private final Map<String, RequestCounter> requests = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long now = Instant.now().toEpochMilli();

        RequestCounter counter = requests.get(ip);

        if (counter == null || now - counter.windowStart > WINDOW_MS) {
            counter = new RequestCounter(now, 1);
            requests.put(ip, counter);
        } else {
            counter.count++;
        }

        if (counter.count > MAX_REQUESTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");

            ApiResponse<Void> apiResponse =
                    ApiResponse.error("Demasiadas peticiones. Intenta más tarde.");

            new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Clase auxiliar
    private static class RequestCounter {
        long windowStart;
        int count;

        RequestCounter(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }
}