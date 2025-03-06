package com.onyxdb.idm.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onyxdb.idm.generated.openapi.models.BadRequestResponse;
import com.onyxdb.idm.generated.openapi.models.InternalServerErrorResponse;
import com.onyxdb.idm.generated.openapi.models.NotFoundResponse;

/**
 * @author ArtemFed
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = org.springframework.web.server.ServerWebInputException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(org.springframework.web.server.ServerWebInputException e) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestResponse> handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<NotFoundResponse> handleNotFoundException(ResourceNotFoundException e) {
        NotFoundResponse notFoundResponse = new NotFoundResponse();
        notFoundResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<InternalServerErrorResponse> handleInternalServerErrorException(Exception e) {
        InternalServerErrorResponse internalServerErrorResponse = new InternalServerErrorResponse();
        internalServerErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(internalServerErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}