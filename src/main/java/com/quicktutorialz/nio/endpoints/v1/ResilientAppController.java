package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.entities.ResponseDto;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ResilientAppController extends Endpoints {

    private final ExternalAPICaller externalAPICaller = new ExternalAPICaller();

    // --- Configuración de Resilience4j ---

    private final CircuitBreaker circuitBreaker = CircuitBreaker.of("CircuitBreakerApi",
            CircuitBreakerConfig.custom()
                    .failureRateThreshold(50)
                    .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .slidingWindowSize(10)
                    .minimumNumberOfCalls(5)
                    .permittedNumberOfCallsInHalfOpenState(3)
                    .waitDurationInOpenState(Duration.ofMillis(10000))
                    .build());

    private final Bulkhead bulkhead = Bulkhead.of("bulkheadApi",
            BulkheadConfig.custom()
                    .maxConcurrentCalls(2)
                    .maxWaitDuration(Duration.ZERO) // 0ms
                    .build());

    private final RateLimiter rateLimiter = RateLimiter.of("rateLimiterApi",
            RateLimiterConfig.custom()
                    .limitRefreshPeriod(Duration.ofSeconds(10))
                    .limitForPeriod(5)
                    .timeoutDuration(Duration.ZERO) // 0ms
                    .build());

    private final Retry retry = Retry.of("retryApi",
            RetryConfig.custom()
                    .maxAttempts(3)
                    .waitDuration(Duration.ofSeconds(2))
                    .retryOnException(e -> e instanceof RuntimeException) // Especifica en qué excepciones reintentar
                    .build());

    // --- Endpoints ---

    @Api(path = "/api/resilience/circuit-breaker", method = "GET", produces = "application/json")
    Action circuitBreakerAction = (HttpServletRequest req, HttpServletResponse res) -> {
        Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, externalAPICaller::callApi);
        try {
            String result = decoratedSupplier.get();
            toJsonResponse(req, res, new ResponseDto(200, result));
        } catch (Exception ex) {
            // Fallback
            toJsonResponse(req, res, new ResponseDto(503, "Circuit Breaker Fallback: El servicio externo no está disponible."));
        }
    };

    @Api(path = "/api/resilience/bulkhead", method = "GET", produces = "application/json")
    Action bulkheadAction = (HttpServletRequest req, HttpServletResponse res) -> {
        Supplier<String> decoratedSupplier = Bulkhead.decorateSupplier(bulkhead, externalAPICaller::callApiWithDelay);
        try {
            String result = decoratedSupplier.get();
            toJsonResponse(req, res, new ResponseDto(200, result));
        } catch (Exception ex) {
            // Fallback
            toJsonResponse(req, res, new ResponseDto(400, "Bulkhead Fallback: Demasiadas llamadas concurrentes."));
        }
    };

    @Api(path = "/api/resilience/rate-limiter", method = "GET", produces = "application/json")
    Action rateLimiterAction = (HttpServletRequest req, HttpServletResponse res) -> {
        Supplier<String> decoratedSupplier = RateLimiter.decorateSupplier(rateLimiter, externalAPICaller::callApiWithDelay2);
        try {
            String result = decoratedSupplier.get();
            toJsonResponse(req, res, new ResponseDto(200, result));
        } catch (Exception ex) {
            // Fallback
            toJsonResponse(req, res, new ResponseDto(400, "Rate Limiter Fallback: Límite de peticiones excedido."));
        }
    };

    @Api(path = "/api/resilience/retry", method = "GET", produces = "application/json")
    Action retryAction = (HttpServletRequest req, HttpServletResponse res) -> {
        Supplier<String> decoratedSupplier = Retry.decorateSupplier(retry, externalAPICaller::callApi);
        try {
            String result = decoratedSupplier.get();
            toJsonResponse(req, res, new ResponseDto(200, result));
        } catch (Exception ex) {
            // Fallback
            toJsonResponse(req, res, new ResponseDto(400, "Fallback after retry: " + ex.getMessage()));
        }
    };

    public ResilientAppController() {
        setEndpoint("/api/resilience/circuit-breaker", circuitBreakerAction);
        setEndpoint("/api/resilience/bulkhead", bulkheadAction);
        setEndpoint("/api/resilience/rate-limiter", rateLimiterAction);
        setEndpoint("/api/resilience/retry", retryAction);
    }
}
