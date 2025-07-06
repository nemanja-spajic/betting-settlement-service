package com.sporty.betting.settlement.common.exception;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ApiFieldError {
    private final String field;
    private final String message;
}

