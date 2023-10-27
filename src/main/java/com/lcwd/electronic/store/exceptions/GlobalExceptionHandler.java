package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler{

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiMessageResponse>resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {

        logger.info(ex.getMessage());

        ApiMessageResponse response = ApiMessageResponse.builder().message(ex.getMessage()).
                success(true).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<Object,String>>methodaArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex)
    {
        Map<Object,String>exceptions = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        logger.info("Message from MethodArgumentNotValidException");
        errors.stream().forEach(objectError -> {

            String field = ((FieldError)objectError).getField();
            String message = objectError.getDefaultMessage();
            logger.info(field+ " "+message);
            exceptions.putIfAbsent(field,message);

        });
        return new ResponseEntity<>(exceptions,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiMessageResponse>propertyReferenceExceptionHandler(PropertyReferenceException ex)
    {

        logger.info(ex.getMessage());

        ApiMessageResponse response = ApiMessageResponse.builder().message(ex.getMessage()).
                success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiMessageResponse>badApiRequestHandler(BadApiRequest ex)
    {

        logger.info(ex.getMessage());

        ApiMessageResponse response = ApiMessageResponse.builder().message(ex.getMessage()).
                success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceptionDuringFolderCreation.class)
    public ResponseEntity<ApiMessageResponse>folderNotCreatedExceptionHandler(ExceptionDuringFolderCreation ex)
    {

        logger.info(ex.getMessage());

        ApiMessageResponse response = ApiMessageResponse.builder().message(ex.getMessage()).
                success(false).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniqueTitleException.class)
    public ResponseEntity<ApiMessageResponse>UniqueTitleExceptionHandler(UniqueTitleException ex)
    {

        logger.info(ex.getMessage());

        ApiMessageResponse response = ApiMessageResponse.builder().message(ex.getMessage()).
                success(false).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
