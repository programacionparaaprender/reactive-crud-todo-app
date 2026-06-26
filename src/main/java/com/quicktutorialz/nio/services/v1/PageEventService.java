package com.quicktutorialz.nio.services.v1;

import com.quicktutorialz.nio.entities.PageEvent;

public class PageEventService {

    private static PageEventService instance;

    private PageEventService() {}

    public static synchronized PageEventService getInstance() {
        if (instance == null) {
            instance = new PageEventService();
        }
        return instance;
    }

    /**
     * Simula el consumo de un PageEvent, similar al Consumer de Spring Cloud Stream.
     * Imprime los detalles del evento en la consola.
     * @param input El PageEvent a procesar.
     */
    public void consumePageEvent(PageEvent input) {
        System.out.println("*****************");
        System.out.println("Consuming Event -> Name: " + input.getName());
        System.out.println("Consuming Event -> User: " + input.getUser());
        System.out.println("Consuming Event -> Duration: " + input.getDuration());
        System.out.println("******************");
    }
}
