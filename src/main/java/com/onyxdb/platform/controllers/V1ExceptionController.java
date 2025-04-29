package com.onyxdb.platform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.exceptions.NotFoundException;
import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;

/**
 * @author foxleren
 */
@ControllerAdvice
public class V1ExceptionController {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(BadRequestException e) {
        BadRequestResponse response = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(NotFoundException e) {
        BadRequestResponse response = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
