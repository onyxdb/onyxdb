package com.onyxdb.platform.mdb.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;
import com.onyxdb.platform.generated.openapi.models.InternalServerErrorResponse;
import com.onyxdb.platform.generated.openapi.models.NotFoundResponse;
import com.onyxdb.platform.generated.openapi.models.UnauthorizedResponse;
import com.onyxdb.platform.idm.models.exceptions.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.exceptions.UnauthorizedException;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.exceptions.NotFoundException;

/**
 * @author foxleren
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<BadRequestResponse> handleBadRequestException(BadRequestException e) {
        BadRequestResponse response = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<BadRequestResponse> handleNotFoundException(NotFoundException e) {
        BadRequestResponse response = new BadRequestResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

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

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<UnauthorizedResponse> handleUnauthorizedException(UnauthorizedException e) {
        UnauthorizedResponse response = new UnauthorizedResponse();
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<InternalServerErrorResponse> handleInternalServerErrorException(Exception e) {
        System.err.println("Internal Server Error: " + e.getMessage());
        InternalServerErrorResponse internalServerErrorResponse = new InternalServerErrorResponse();
        internalServerErrorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(internalServerErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
