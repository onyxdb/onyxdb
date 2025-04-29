package com.onyxdb.platform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;

/**
 * @author foxleren
 */
@ControllerAdvice
public class V1ExceptionController {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(BadRequestException e) {
        BadRequestResponse badRequestResponse = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
