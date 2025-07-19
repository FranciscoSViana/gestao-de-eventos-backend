package com.fsv.gestaodeeventosbackend.api.assembler;

import com.fsv.gestaodeeventosbackend.api.model.EventModel;
import com.fsv.gestaodeeventosbackend.domain.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventModelAssembler {

    private final ModelMapper modelMapper;

    public EventModel toModel(Event event) {
        EventModel eventModel = modelMapper.map(event, EventModel.class);

        if (event.getEventTime() != null) {
            LocalDateTime localDateTime = event.getEventTime()
                    .atZoneSameInstant(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
                    .toLocalDateTime();

            eventModel.setEventTime(localDateTime);
        }

        return eventModel;
    }

    public List<EventModel> toCollectionModel(List<Event> events) {
        return events.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
