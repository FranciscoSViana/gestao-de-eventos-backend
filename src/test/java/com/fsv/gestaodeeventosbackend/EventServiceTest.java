package com.fsv.gestaodeeventosbackend;

import com.fsv.gestaodeeventosbackend.domain.Event;
import com.fsv.gestaodeeventosbackend.domain.repository.EventRepository;
import com.fsv.gestaodeeventosbackend.domain.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void deveSalvarEventoComOffsetUtc() {
        Event event = new Event();
        event.setEventTime(OffsetDateTime.now(ZoneOffset.of("-03:00")));

        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));

        Event saved = eventService.saveEvent(event);

        assertEquals(ZoneOffset.UTC, saved.getEventTime().getOffset());
    }

    @Test
    void deveBuscarTodosEventos() {
        List<Event> events = List.of(new Event(), new Event());
        Page<Event> pageEvents = new PageImpl<>(events);

        EventRepository eventRepository = mock(EventRepository.class);
        EventService eventService = new EventService(eventRepository);

        when(eventRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageEvents);

        Pageable pageable = Pageable.unpaged();

        Page<Event> result = eventService.getAllEvents(pageable);

        assertEquals(2, result.getContent().size());
        verify(eventRepository, times(1)).findAll(pageable);
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontrado() {
        Long idInexistente = 999L;
        when(eventRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventService.findOrFail(idInexistente));
    }

}
