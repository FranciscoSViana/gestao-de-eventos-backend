package com.fsv.gestaodeeventosbackend;

import com.fsv.gestaodeeventosbackend.domain.Event;
import com.fsv.gestaodeeventosbackend.domain.repository.EventRepositoty;
import com.fsv.gestaodeeventosbackend.domain.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepositoty eventRepositoty;

    @InjectMocks
    private EventService eventService;

    @Test
    void deveSalvarEventoComOffsetUtc() {
        Event event = new Event();
        event.setEventTime(OffsetDateTime.now(ZoneOffset.of("-03:00")));

        when(eventRepositoty.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));

        Event saved = eventService.saveEvent(event);

        assertEquals(ZoneOffset.UTC, saved.getEventTime().getOffset());
    }

    @Test
    void deveBuscarTodosEventos() {
        List<Event> eventos = List.of(new Event(), new Event());

        when(eventRepositoty.findAll()).thenReturn(eventos);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontrado() {
        Long idInexistente = 999L;
        when(eventRepositoty.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventService.findOrFail(idInexistente));
    }

}
