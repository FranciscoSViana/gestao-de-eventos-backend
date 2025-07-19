package com.fsv.gestaodeeventosbackend.api.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class EventInput {

    private String title;

    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime eventTime;

    private String eventLocal;
}
