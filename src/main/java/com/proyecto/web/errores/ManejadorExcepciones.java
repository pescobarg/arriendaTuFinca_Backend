package com.proyecto.web.errores;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Calendar;

@RestControllerAdvice
public class ManejadorExcepciones {
    public static final String ERROR_INTERNO = "Ha ocurrido un error interno";


    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<MensajeError> manejarExcepcion(ResourceNotFound e) {
        e.printStackTrace();
        MensajeError mensajeError = new MensajeError("Recurso no encontrado", Calendar.getInstance().getTime(), e.getMessage());
        return new ResponseEntity<>(mensajeError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MensajeError> manejarExcepcion(DataIntegrityViolationException e) {
        e.printStackTrace();
        MensajeError mensajeError = new MensajeError("Un valor no repetible ya se encuentra almacenado", Calendar.getInstance().getTime(), e.getMessage());
        return new ResponseEntity<>(mensajeError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeError> manejarExcepcion(IllegalArgumentException e) {
        e.printStackTrace();
        MensajeError mensajeError = new MensajeError("Valor no valido", Calendar.getInstance().getTime(), e.getMessage());
        return new ResponseEntity<>(mensajeError, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensajeError> manejarExcepcion(HttpMessageNotReadableException e) {
        e.printStackTrace();
        MensajeError mensajeError = new MensajeError("Error al leer datos", Calendar.getInstance().getTime(), e.getMessage());
        return new ResponseEntity<>(mensajeError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeError> manejarExcepcion(Exception e) {
        e.printStackTrace();
        MensajeError mensajeError = new MensajeError("Error en la peticion", Calendar.getInstance().getTime(), e.getMessage());
        return new ResponseEntity<>(mensajeError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
