package com.fsv.gestaodeeventosbackend.domain.service;

import com.fsv.gestaodeeventosbackend.domain.Event;
import com.fsv.gestaodeeventosbackend.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event saveEvent(Event event) {
        event.setEventTime(event.getEventTime().withOffsetSameInstant(ZoneOffset.UTC));
        return eventRepository.save(event);
    }

    public void delete(Long eventId) {
        Event event = findOrFail(eventId);
        event.setSoftDelete(true);
        eventRepository.save(event);
    }

    public Event findOrFail(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException(eventId.toString()));
    }
}
