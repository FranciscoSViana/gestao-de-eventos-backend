package com.fsv.gestaodeeventosbackend.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class Problem {

    private Integer status;
    private OffsetDateTime timestamp;
    private String title;
    private List<FieldValidationError> fields;
}
