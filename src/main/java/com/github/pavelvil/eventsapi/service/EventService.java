package com.github.pavelvil.eventsapi.service;

import com.github.pavelvil.eventsapi.entity.BaseEntity;
import com.github.pavelvil.eventsapi.entity.Event;
import com.github.pavelvil.eventsapi.event.model.EmailNotificationEvent;
import com.github.pavelvil.eventsapi.exception.AccessDeniedException;
import com.github.pavelvil.eventsapi.exception.EntityNotFoundException;
import com.github.pavelvil.eventsapi.model.EventFilterModel;
import com.github.pavelvil.eventsapi.repository.EventRepository;
import com.github.pavelvil.eventsapi.repository.LocationRepository;
import com.github.pavelvil.eventsapi.repository.ScheduleRepository;
import com.github.pavelvil.eventsapi.repository.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    private final ScheduleRepository scheduleRepository;

    private final LocationRepository locationRepository;

    private final OrganizationService organizationService;

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Page<Event> filter(EventFilterModel filterModel) {
        return eventRepository.findAll(
                EventSpecification.withFilter(filterModel),
                filterModel.getPage().toPageRequest()
        );
    }

    public Event getById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Event with id {0} not found!", eventId)
        ));
    }

    @Transactional
    public Event create(Event event) {
        event.setCategories(categoryService.upsertCategories(event.getCategories()));

        event.setSchedule(scheduleRepository.save(event.getSchedule()));

        var location = locationRepository.findByCityAndStreet(
                        event.getLocation().getCity(), event.getLocation().getStreet()
                )
                .orElseGet(() -> locationRepository.save(event.getLocation()));
        event.setLocation(location);

        var organization = organizationService.findById(event.getOrganization().getId());

        if (event.getOrganization().isNotSameOwner(organization.getOwner().getId())) {
            throw new AccessDeniedException("User is not owner!");
        }

        organization.addEvent(event);
        event.setOrganization(organization);

        var savedEvent = eventRepository.save(event);

        eventPublisher.publishEvent(new EmailNotificationEvent(
                this,
                event.getCategories().stream().map(BaseEntity::getId).collect(Collectors.toSet()),
                organization.getId(),
                event.getName()
        ));

        return savedEvent;
    }

    @Transactional
    public Event update(Long eventId, Event eventForUpdate) {
        var currentEvent = getById(eventId);

        if (eventForUpdate.getName() != null &&
                !Objects.equals(eventForUpdate.getName(), currentEvent.getName())) {
            currentEvent.setName(eventForUpdate.getName());
        }
        if (eventForUpdate.getStartTime() != null &&
                !Objects.equals(eventForUpdate.getStartTime(), currentEvent.getEndTime())) {
            currentEvent.setStartTime(eventForUpdate.getStartTime());
        }
        if (eventForUpdate.getEndTime() != null &&
                !Objects.equals(eventForUpdate.getEndTime(), currentEvent.getEndTime())) {
            currentEvent.setEndTime(eventForUpdate.getEndTime());
        }

        var currentSchedule = currentEvent.getSchedule();
        var updatedSchedule = eventForUpdate.getSchedule();
        if (updatedSchedule != null && StringUtils.isNoneBlank(updatedSchedule.getDescription()) &&
                !Objects.equals(currentSchedule.getDescription(), updatedSchedule.getDescription())) {
            currentSchedule.setDescription(updatedSchedule.getDescription());
        }

        if (!CollectionUtils.isEmpty(eventForUpdate.getCategories())) {
            currentEvent.setCategories(categoryService.upsertCategories(eventForUpdate.getCategories()));
        }

        return eventRepository.save(currentEvent);
    }

    @Transactional
    public boolean addParticipant(Long eventId, Long participantId) {
        var event = getById(eventId);
        var participant = userService.findById(participantId);
        var isAdded = event.addParticipant(participant);

        if (!isAdded) {
            return false;
        }

        eventRepository.save(event);

        return true;
    }

    @Transactional
    public boolean removeParticipant(Long eventId, Long participantId) {
        var event = getById(eventId);
        var participant = userService.findById(participantId);
        var isRemoved = event.removeParticipant(participant);

        if (!isRemoved) {
            return false;
        }

        eventRepository.save(event);

        return true;
    }

    @Transactional
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public boolean hasParticipant(Long eventId, Long participantId) {
        return eventRepository.existsByIdAndParticipantsId(eventId, participantId);
    }

    public boolean isEventCreator(Long eventId, Long userId) {
        return eventRepository.existsByIdAndOrganizationOwnerId(eventId, userId);
    }
}
