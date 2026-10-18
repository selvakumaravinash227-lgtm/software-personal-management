package edu.spms.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOG=LoggerFactory.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<Map<String,String>> invalid(IllegalArgumentException ex){return ResponseEntity.badRequest().body(Map.of("message",ex.getMessage()));}
    @ExceptionHandler(AccessDeniedException.class) public ResponseEntity<Map<String,String>> denied(AccessDeniedException ex){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",ex.getMessage()));}
    @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<Map<String,String>> status(ResponseStatusException ex){return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message",ex.getReason()==null?"The request could not be completed.":ex.getReason()));}
    @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<Map<String,String>> conflict(DataIntegrityViolationException ex){LOG.warn("SPMS record conflict: database constraint rejected the request.",ex);return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","A duplicate record or related data constraint prevented this change."));}
    @ExceptionHandler({DataAccessException.class,CannotCreateTransactionException.class}) public ResponseEntity<Map<String,String>> database(Exception ex){LOG.error("Database operation failed in Software Personnel Management System",ex);return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("message","Database connection failed. Check DB_HOST, DB_PORT, DB_NAME, DB_USERNAME, and DB_PASSWORD in the project .env file, then import database/schema.sql and database/sample_data.sql if SPMS_DB is new."));}
    @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,String>> unexpected(Exception ex){LOG.error("Unexpected SPMS API failure",ex);return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","The request could not be completed. Please try again or contact the administrator."));}
}
