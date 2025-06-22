package org.iftm.biblioteca.controller.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.iftm.biblioteca.service.exceptions.CategoriaComLivrosException;
import org.iftm.biblioteca.service.exceptions.IsbnDuplicadoException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {

    // Inner class for standard error response
    // Você pode mover esta classe para seu próprio arquivo se preferir
    public static class StandardError {
        private Instant timestamp;
        private Integer status;
        private String error;
        private String message;
        private String path;
        private List<String> validationErrors;

        public StandardError() {
        }

        // Getters e Setters omitidos para brevidade, mas devem ser incluídos
        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<String> getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(List<String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<StandardError> resourceNotFound(RecursoNaoEncontradoException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Recurso não encontrado");
        err.setMessage(e.getMessage() != null ? e.getMessage() : "O recurso solicitado não foi encontrado.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler({ RegraDeNegocioException.class, CategoriaComLivrosException.class })
    public ResponseEntity<StandardError> businessRuleViolation(RuntimeException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Violação de regra de negócio");
        err.setMessage(e.getMessage() != null ? e.getMessage() : "Uma regra de negócio foi violada.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler({ NomeDuplicadoException.class, IsbnDuplicadoException.class,
            DataIntegrityViolationException.class })
    public ResponseEntity<StandardError> dataIntegrity(RuntimeException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setError("Violação de integridade de dados");
        String message = e.getMessage();
        if (e instanceof DataIntegrityViolationException) {
            message = "Erro de integridade de dados. Verifique se os dados fornecidos são únicos e se as referências a outras entidades são válidas.";
        }
        err.setMessage(message != null ? message : "Ocorreu uma violação de integridade dos dados.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Erro de validação");
        err.setMessage("Um ou mais campos falharam na validação.");
        err.setPath(request.getRequestURI());
        List<String> validationMessages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            validationMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        err.setValidationErrors(validationMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Requisição malformada");
        err.setMessage("Não foi possível ler a requisição. Verifique se o JSON está bem formado e os tipos de dados estão corretos. Detalhe: " + e.getLocalizedMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }



    /**
     * Handler genérico para exceções não tratadas.
     * Captura qualquer exceção não tratada pelos handlers específicos e retorna um erro 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericException(Exception e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError("Erro interno do servidor");
        err.setMessage("Ocorreu um erro inesperado no processamento da sua requisição. Detalhe: " + e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}