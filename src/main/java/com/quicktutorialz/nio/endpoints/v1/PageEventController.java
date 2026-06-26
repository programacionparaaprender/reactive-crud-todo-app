package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.entities.PageEvent;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.services.v1.PageEventService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

public class PageEventController extends Endpoints {

    private final PageEventService pageEventService = PageEventService.getInstance();

    @Api(path = "/api/publish/{topic}/{name}", method = "GET", produces = "application/json")
    Action publish = (HttpServletRequest request, HttpServletResponse response) -> {
        String topic = getPathVariables(request).get("topic");
        String name = getPathVariables(request).get("name");

        PageEvent pageEvent = new PageEvent(name, Math.random() > 0.5 ? "A1" : "A3", new Date(), new Random().nextInt(9000));

        // Simulación del envío de eventos, ya que StreamBridge no está disponible.
        System.out.println("Enviando al topic '" + topic + "': " + pageEvent);
        System.out.println("Enviando a 'pageEventProducer-out-0': " + pageEvent);
        System.out.println("Enviando a 'pageEventConsumer-in-0': " + pageEvent);

        
        pageEventService.consumePageEvent(pageEvent);

        toJsonResponse(request, response, new ResponseDto(200, pageEvent));
    };

    public PageEventController() {
        setEndpoint("/api/publish/{topic}/{name}", publish);
    }
}
