package com.example.carros.api.exeception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class ExceptionConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity errorNotFound(Exception e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity errorBadRequest(Exception ex){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity accessDenied(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error("Acesso negado"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        return new ResponseEntity<>(new Error("Operação não permitida"), HttpStatus.METHOD_NOT_ALLOWED);
    }
}

//Classe para retornar uma mensagem de erro
class Error{
    private String error;
    Error(String error){
        this.error = error;
    }
    public String getError(){ return error;}
}
