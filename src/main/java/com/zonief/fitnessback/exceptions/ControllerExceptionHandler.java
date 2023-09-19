package com.zonief.fitnessback.exceptions;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequest.class)
  public ProblemDetail resourceNotFoundException(BadRequest ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        ex.getMessage());
    problemDetail.setTitle("Bad Request");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(TechnicalError.class)
  public ProblemDetail handleTechnicalError(TechnicalError ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
    problemDetail.setTitle("Technical Error");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

}
