package com.github.pavelvil.eventsapi.event.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

@Getter
public class EmailNotificationEvent extends ApplicationEvent {

    private final Collection<Long> categories;

    private final Long organization;

    private final String eventName;

    public EmailNotificationEvent(Object source, Collection<Long> categories, Long organization, String eventName) {
        super(source);
        this.categories = categories;
        this.organization = organization;
        this.eventName = eventName;
    }
}
