package com.fsv.gestaodeeventosbackend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100, message = "O titulo deve ter no máximo 100 caracteres.")
    private String title;

    @Size(max = 1000, message = "A descricao deve ter no máximo 1000 caracteres.")
    private String description;

    @Column(columnDefinition = "TIMESTAMP(0)")
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ssXXX")
    private OffsetDateTime eventTime;

    @Size(max = 200, message = "O local deve ter no máximo 200 caracteres.")
    private String eventLocal;

    private Boolean softDelete = false;
}
