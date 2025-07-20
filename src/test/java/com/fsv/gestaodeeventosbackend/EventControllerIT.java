package com.fsv.gestaodeeventosbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsv.gestaodeeventosbackend.domain.repository.EventRepositoty;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // limpa H2 entre testes
class EventControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepositoty eventRepository;

    @Test
    void deveCriarEventoEListarComSucesso() throws Exception {
        String json = """
        {
          "title": "Evento Teste",
          "description": "Descrição do Evento",
          "eventTime": "20-07-2025 10:00",
          "eventLocal": "São Paulo"
        }
        """;

        // POST /api/events
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Evento Teste"))
                .andExpect(jsonPath("$.description").value("Descrição do Evento"))
                .andExpect(jsonPath("$.eventLocal").value("São Paulo"));

        // GET /api/events
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Evento Teste"));
    }
}
