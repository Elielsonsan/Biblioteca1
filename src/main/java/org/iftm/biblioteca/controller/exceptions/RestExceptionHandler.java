package org.iftm.biblioteca.controller.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.iftm.biblioteca.service.exceptions.CategoriaComLivrosException;
import org.iftm.biblioteca.service.exceptions.IsbnDuplicadoException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Classe interna para representar um erro de validação de campo.
     * Usada para serializar uma lista de erros no formato JSON, que o frontend pode consumir.
     */
    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
    }


    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<StandardError> resourceNotFound(RecursoNaoEncontradoException e, HttpServletRequest request) {
        String error = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        // Garante que a mensagem não seja nula, fornecendo um padrão se necessário.
        String message = e.getMessage() != null ? e.getMessage() : "O recurso solicitado não foi encontrado.";
        StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({ RegraDeNegocioException.class, CategoriaComLivrosException.class })
    public ResponseEntity<StandardError> businessRuleViolation(RuntimeException e, HttpServletRequest request) {
        String error = "Violação de regra de negócio";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        // Garante que a mensagem não seja nula, fornecendo um padrão se necessário.
        String message = e.getMessage() != null ? e.getMessage() : "A operação não pôde ser concluída devido a uma regra de negócio.";
        StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({ NomeDuplicadoException.class, IsbnDuplicadoException.class,
            DataIntegrityViolationException.class })
    public ResponseEntity<StandardError> dataIntegrity(RuntimeException e, HttpServletRequest request) {
        String error = "Violação de integridade de dados";
        HttpStatus status = HttpStatus.CONFLICT;
        String message; // A mensagem será definida abaixo

        if (e instanceof DataIntegrityViolationException) {
            // Inicializa com uma mensagem padrão para garantir que sempre tenha um valor.
            message = "A operação violou uma restrição de dados. Verifique se o item não está em uso ou se os dados são únicos.";
            // Tenta obter a causa raiz para uma mensagem mais específica
            Throwable rootCause = e.getCause();
            if (rootCause != null) {
                Throwable causeOfRootCause = rootCause.getCause();
                if (causeOfRootCause != null) {
                    String causeMessage = causeOfRootCause.getMessage();
                    if (causeMessage != null) {
                        String lowerCaseCauseMessage = causeMessage.toLowerCase();
                        if (lowerCaseCauseMessage.contains("unique constraint")
                                || lowerCaseCauseMessage.contains("duplicate entry")) {
                            message = "Já existe um registro com os dados informados (ex: Título ou ISBN já cadastrado).";
                        } else { // Provavelmente uma violação de chave estrangeira (ex: deletar categoria com livros)
                            message = "A operação não pode ser concluída porque o item está sendo usado por outro registro.";
                        }
                    }
                }
            }
        } else {
            // Garante que a mensagem não seja nula para outras exceções de integridade
            message = (e != null && e.getMessage() != null) ? e.getMessage() : "Violação de integridade de dados.";
        }

        StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> validation(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    String defaultMessage = fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Valor inválido.";
                    return new ValidationError(fieldError.getField(), defaultMessage);
                })
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        String error = "Requisição malformada";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Não foi possível ler a requisição. Verifique se o JSON está bem formado e os tipos de dados estão corretos.";
        StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    /**
     * Handler genérico para exceções não tratadas.
     * Captura qualquer exceção não tratada pelos handlers específicos e retorna um erro 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericException(Exception e, HttpServletRequest request) {
        String error = "Erro interno do servidor";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        // Evita expor detalhes de exceções internas ao cliente em um handler genérico
        String message = "Ocorreu um erro inesperado no processamento da sua requisição.";
        
        StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}