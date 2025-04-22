package com.onyxdb.platform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.generated.openapi.models.V1BadRequestResponse;

/**
 * @author foxleren
 */
@ControllerAdvice
public class V1ExceptionController {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<V1BadRequestResponse> handleBadRequestException(BadRequestException e) {
        V1BadRequestResponse badRequestResponse = new V1BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
