package com.fsv.gestaodeeventosbackend.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class EventModel {

    @Size(max = 100, message = "O titulo deve ter no máximo 100 caracteres.")
    private String title;

    @Size(max = 1000, message = "A descricao deve ter no máximo 1000 caracteres.")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventTime;

    @Size(max = 200, message = "O local deve ter no máximo 200 caracteres.")
    private String eventLocal;
}
