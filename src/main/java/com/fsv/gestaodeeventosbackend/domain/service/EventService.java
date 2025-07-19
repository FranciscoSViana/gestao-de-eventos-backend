package com.fsv.gestaodeeventosbackend.domain.service;

import com.fsv.gestaodeeventosbackend.domain.Event;
import com.fsv.gestaodeeventosbackend.domain.repository.EventRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepositoty eventRepositoty;

    public List<Event> getAllEvents() {
        return eventRepositoty.findAll();
    }

    public Event saveEvent(Event event) {
        event.setEventTime(event.getEventTime().withOffsetSameInstant(ZoneOffset.UTC));
        return eventRepositoty.save(event);
    }

    public void delete(Long eventId) {
        Event event = findOrFail(eventId);
        event.setSoftDelete(true);
        eventRepositoty.save(event);
    }

    public Event findOrFail(Long eventId) {
        return eventRepositoty.findById(eventId)
                .orElseThrow(() -> new RuntimeException(eventId.toString()));
    }
}
