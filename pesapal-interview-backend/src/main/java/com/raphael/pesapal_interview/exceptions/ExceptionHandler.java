/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raphael.pesapal_interview.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author mo
 */
@ControllerAdvice
public class ExceptionHandler {
    @Builder
    public record ErrorResponse(String message, String detail, LinkedHashMap<String, String> errors) {
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().parallelStream()
                .forEach(e -> {
                    errors.put(e.getCode(), e.getDefaultMessage());
                });
        return new ResponseEntity<>(new ErrorResponse("ERROR", "Arguments not valid", errors), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(HandlerMethodValidationException ex) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        for (ParameterValidationResult parameterValidationResult : ex.getParameterValidationResults()) {
            List<MessageSourceResolvable> resolvableErrors = parameterValidationResult.getResolvableErrors();
            for (MessageSourceResolvable e : resolvableErrors) {
                errors.put("Detail", e.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(new ErrorResponse("ERROR", "Arguments validation failed", errors), HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintValidationException(
            ConstraintViolationException ex) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String errorField = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(errorField, errorMessage);

        });

        return new ResponseEntity<>(new ErrorResponse("ERROR", "Constraint validation failed", errors), HttpStatus.BAD_REQUEST);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.put(ex.getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("ERROR", "Type of values supplied don't match the expected type", errors), HttpStatus.NOT_IMPLEMENTED);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> BadRequestExceptionHandler(BadRequestException ex) {
        LinkedHashMap<String, String> errors = new LinkedHashMap<>();
        errors.put("Details", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder().message("ERROR").detail("Bad Request").errors(errors).build(), HttpStatus.BAD_REQUEST);
    }


    public static String extractErrorLine(String text) {
        int errorIndex = text.indexOf("ERROR:");
        if (errorIndex == -1) return null;


        int newlineIndex = text.indexOf("\n", errorIndex);

        int detailIndex = text.indexOf("\".", newlineIndex);
        if (newlineIndex == -1) {
            // If there's no newline, return till end
            return text.substring(errorIndex);
        } else {
            if (detailIndex != -1) {
                return text.substring(errorIndex + 7, newlineIndex) + text.substring(newlineIndex, detailIndex);
            }
            return text.substring(errorIndex + 7, newlineIndex);
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) throws JsonProcessingException {
        var errors = new LinkedHashMap<String, String>();
        var detail = "";
        if (ex.getMessage().contains("violates unique constraint")) {
            detail = "Unique value required";
        } else if (ex.getMessage().contains("violates foreign key constraint") && ex.getMessage().contains("not present")) {
            detail = "Link via foreign key error as supplied value doesn't exist in target table";
        } else if (ex.getMessage().contains("violates foreign key constraint")) {
            detail = "Record currently in use in other records";
        } else {
            detail = "Data integrity violation";
        }

        return new ResponseEntity<>(ErrorResponse.builder().message("ERROR").detail(detail).errors(errors).build(), HttpStatus.BAD_REQUEST);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> ConstraintViolationExceptionHandler(org.hibernate.exception.ConstraintViolationException ex) throws JsonProcessingException {
        var errors = new LinkedHashMap<String, String>();
        var detail = "";
        if (ex.getMessage().contains("violates unique constraint")) {
            detail = "Unique value required";
        } else if (ex.getMessage().contains("violates foreign key constraint") && ex.getMessage().contains("not present")) {
            detail = "Link via foreign key error as supplied value doesn't exist in target table";
        } else if (ex.getMessage().contains("violates foreign key constraint")) {
            detail = "Record currently in use in other records";
        } else {
            detail = "Data integrity violation";
        }

        return new ResponseEntity<>(ErrorResponse.builder().message("ERROR").detail(detail).errors(errors).build(), HttpStatus.BAD_REQUEST);
    }



    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var errors = new LinkedHashMap<String, String>();
        errors.put("Details", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("ERROR", "JSON Payload error", errors), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
        var errors = new LinkedHashMap<String, String>();

        return new ResponseEntity<>(new ErrorResponse("ERROR", "DB error", errors), HttpStatus.BAD_REQUEST);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        var errors = new LinkedHashMap<String, String>();
        errors.put("Detail", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("ERROR", "Wrong value provided", errors), HttpStatus.BAD_REQUEST);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterExceptionException(MissingServletRequestParameterException ex) {
        var errors = new LinkedHashMap<String, String>();
        errors.put("Detail", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("ERROR", "Missing request attribute", errors), HttpStatus.BAD_REQUEST);
    }
}
