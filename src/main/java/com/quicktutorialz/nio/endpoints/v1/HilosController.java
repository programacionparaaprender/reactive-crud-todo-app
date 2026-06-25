package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.entities.ResponseDto;
import io.reactivex.rxjava3.core.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HilosController extends Endpoints {

    // Java 21: Usando Virtual Threads para escalabilidad masiva
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    // ThreadPoolExecutor para un control más granular
    private final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
            2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10)
    );

    @Api(path = "/api/hilos/executor-service", method = "GET", produces = "application/json")
    Action useExecutorService = (HttpServletRequest request, HttpServletResponse response) -> {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(java.time.Duration.ofSeconds(2));
                return "Tarea completada usando ExecutorService en el hilo: " + Thread.currentThread().getName();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Tarea interrumpida usando ExecutorService";
            }
        }, executorService);

        try {
            String result = future.get(); // Esperamos a que el futuro se complete
            toJsonResponse(request, response, new ResponseDto(200, result));
        } catch (Exception e) {
            toJsonResponse(request, response, new ResponseDto(500, e.getMessage()));
        }
    };

    @Api(path = "/api/hilos/thread-pool-executor", method = "GET", produces = "application/json")
    Action useThreadPoolExecutor = (HttpServletRequest request, HttpServletResponse response) -> {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                return "Tarea completada usando ThreadPoolExecutor en el hilo: " + Thread.currentThread().getName();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Tarea interrumpida usando ThreadPoolExecutor";
            }
        }, threadPoolExecutor);

        try {
            String result = future.get(); // Esperamos a que el futuro se complete
            toJsonResponse(request, response, new ResponseDto(200, result));
        } catch (Exception e) {
            toJsonResponse(request, response, new ResponseDto(500, e.getMessage()));
        }
    };

    @Api(path = "/api/hilos/rxjava", method = "GET", produces = "application/json")
    Action useRxJava = (HttpServletRequest request, HttpServletResponse response) -> {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Iniciando flujo RxJava ---\n");

        Observable<String> saludoObservable = Observable.create(emitter -> {
            try {
                emitter.onNext("Hola");
                Thread.sleep(500);
                emitter.onNext("mundo");
                Thread.sleep(500);
                emitter.onNext("RxJava con Maven");
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        saludoObservable.blockingSubscribe(
                item -> sb.append("Recibido: ").append(item).append("\n"),
                error -> sb.append("Error: ").append(error.getMessage()).append("\n"),
                () -> sb.append("¡Completado!")
        );

        // En lugar de devolver un String plano, lo envolvemos en nuestro DTO de respuesta
        toJsonResponse(request, response, new ResponseDto(200, sb.toString()));
    };

    public HilosController() {
        setEndpoint("/api/hilos/executor-service", useExecutorService);
        setEndpoint("/api/hilos/thread-pool-executor", useThreadPoolExecutor);
        setEndpoint("/api/hilos/rxjava", useRxJava);
    }
}
