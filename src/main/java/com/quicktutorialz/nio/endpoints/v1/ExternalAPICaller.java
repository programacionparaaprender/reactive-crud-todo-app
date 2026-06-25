package com.quicktutorialz.nio.endpoints.v1;

import java.util.Random;

/**
 * Simula llamadas a una API externa que puede fallar o tener retrasos.
 */
public class ExternalAPICaller {

    /**
     * Simula una llamada a una API que a veces falla.
     * Lanza una RuntimeException el 50% de las veces para probar los patrones de CircuitBreaker y Retry.
     * @return Un mensaje de éxito si la llamada no falla.
     */
    public String callApi() {
        if (new Random().nextBoolean()) {
            throw new RuntimeException("Fallo simulado en la API externa.");
        }
        return "Llamada a la API externa exitosa.";
    }

    /**
     * Simula una llamada a una API que tiene un retraso.
     * Usado para probar el patrón Bulkhead.
     * @return Un mensaje de éxito después de un retraso.
     */
    public String callApiWithDelay() {
        try {
            Thread.sleep(2000); // Retraso de 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Llamada a la API con retraso (2s) exitosa.";
    }

    /**
     * Simula otra llamada a una API con un retraso diferente.
     * Usado para probar el patrón RateLimiter.
     * @return Un mensaje de éxito después de un retraso.
     */
    public String callApiWithDelay2() {
        // El RateLimiter se probará con llamadas rápidas, por lo que no es necesario un gran retraso aquí.
        return "Llamada a la API para Rate Limiter exitosa.";
    }
}