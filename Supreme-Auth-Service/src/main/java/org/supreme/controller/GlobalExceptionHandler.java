package org.supreme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.supreme.exceptions.InvalidRequestException;
import org.supreme.exceptions.ItemExistsException;
import org.supreme.exceptions.ItemNotFoundException;
import org.supreme.exceptions.UnauthorisedRequestException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<Object> handle(InvalidRequestException e) {
    return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(ItemExistsException.class)
  public ResponseEntity<Object> handle2(ItemExistsException e) {
    return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<Object> handle3(ItemNotFoundException e) {
    return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(UnauthorisedRequestException.class)
  public ResponseEntity<Object> handle4(UnauthorisedRequestException e) {
    return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handle4(MissingServletRequestParameterException e) {
    return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handle4(Exception e) {
    return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error!"));
  }
}
