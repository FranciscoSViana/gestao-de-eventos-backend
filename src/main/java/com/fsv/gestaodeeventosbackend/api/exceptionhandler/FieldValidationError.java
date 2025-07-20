package com.fsv.gestaodeeventosbackend.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldValidationError {

    private String name;
    private String message;
}
