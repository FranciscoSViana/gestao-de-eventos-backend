package com.fsv.gestaodeeventosbackend.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fsv.gestaodeeventosbackend.api.assembler.EventInputDisassembler;
import com.fsv.gestaodeeventosbackend.api.assembler.EventModelAssembler;
import com.fsv.gestaodeeventosbackend.api.model.EventModel;
import com.fsv.gestaodeeventosbackend.api.model.input.EventInput;
import com.fsv.gestaodeeventosbackend.domain.Event;
import com.fsv.gestaodeeventosbackend.domain.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final SmartValidator smartValidator;

    private final EventService eventService;

    private final EventModelAssembler eventModelAssembler;

    private final EventInputDisassembler eventInputDisassembler;

    @GetMapping
    public List<EventModel> getAll() {

        return eventModelAssembler.toCollectionModel(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public EventModel findEvent(@PathVariable Long eventId) {
        Event event = eventService.findOrFail(eventId);

        return eventModelAssembler.toModel(event);
    }

    @PostMapping
    public EventModel saveEvent(@RequestBody EventInput eventInput) {
        try {

            Event event = eventInputDisassembler.toDomainObject(eventInput);

            return eventModelAssembler.toModel(eventService.saveEvent(event));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{eventId}")
    public EventModel update(@PathVariable Long eventId,  @RequestBody EventInput eventInput) {
        Event event = eventService.findOrFail(eventId);

        eventInputDisassembler.copyToDomainObject(eventInput, event);

        try {
            return eventModelAssembler.toModel(eventService.saveEvent(event));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/{eventId}")
    public EventModel updateParcial(@PathVariable Long eventId, @RequestBody Map<String, Object> fields, HttpServletRequest servletRequest) {
        Event event = eventService.findOrFail(eventId);

        merge(fields, event, servletRequest);

        return update(eventId, eventInputDisassembler.toInputObject(event));
    }

//    private void validate(Event event, String objectName) {
//        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(event, objectName);
//        smartValidator.validate(event, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            throw new
//        }
//
//    }

    private static void merge(Map<String, Object> originData, Event event, HttpServletRequest servletRequest) {
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(servletRequest);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            if (originData.containsKey("eventTime")) {
                String date = originData.get("eventTime").toString();

                OffsetDateTime offsetDateTime;

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
                    offsetDateTime = localDateTime.atOffset(ZoneOffset.of("-03:00"));
                } catch (DateTimeParseException e) {
                    try {
                        // Depois tenta com "dd-MM-yyyy HH:mm"
                        DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                        LocalDateTime localDateTime = LocalDateTime.parse(date, altFormatter);
                        offsetDateTime = localDateTime.atOffset(ZoneOffset.of("-03:00"));
                    } catch (DateTimeParseException e2) {
                        throw new IllegalArgumentException("Formato de data/hora inválido para 'eventTime'. Esperado: yyyy-MM-dd HH:mm ou dd-MM-yyyy HH:mm");
                    }
                }

                DateTimeFormatter formatterIso = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX");
                String eventTimeFormatted = offsetDateTime.format(formatterIso);
                originData.put("eventTime", eventTimeFormatted);
            }

            Event originEvent = objectMapper.convertValue(originData, Event.class);

            originData.forEach((name, value) -> {
                Field field = ReflectionUtils.findField(Event.class, name);
                field.setAccessible(true);

                Object newValue = ReflectionUtils.getField(field, originEvent);

                System.out.println(name + " = " + value + " = " + newValue);

                ReflectionUtils.setField(field, event, newValue);
            });
        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, servletServerHttpRequest);
        }
    }
}
