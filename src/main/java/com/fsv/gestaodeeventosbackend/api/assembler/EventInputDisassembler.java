package com.fsv.gestaodeeventosbackend.api.assembler;

import com.fsv.gestaodeeventosbackend.api.model.input.EventInput;
import com.fsv.gestaodeeventosbackend.domain.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class EventInputDisassembler {

    private final ModelMapper modelMapper;

    public Event toDomainObject(EventInput eventInput) {
        Event event = modelMapper.map(eventInput, Event.class);
        if (eventInput.getEventTime() != null) {
            event.setEventTime(eventInput.getEventTime().atOffset(ZoneOffset.of("-03:00")));
        }
        return event;
    }

    public void copyToDomainObject(EventInput eventInput, Event event) {
        modelMapper.map(eventInput, eventInput);
    }

    public EventInput toInputObject(Event event) {
        return modelMapper.map(event, EventInput.class);
    }
}
