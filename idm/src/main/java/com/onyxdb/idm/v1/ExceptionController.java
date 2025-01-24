package com.onyxdb.idm.v1;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author ao.fedorov
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(BadRequestException e) {
        BadRequestResponse badRequestResponse = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }
}
