package io.github.thesaint14.job;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice 
public class ApiExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSqlException(SQLException e){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Database Error: " + e.getMessage());
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<String> handleBadInput(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Input: " + e.getMessage());
    }
    
}
