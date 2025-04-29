package com.cltech.assistencia_tecnica.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI(), null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraNegocio(RegraNegocioException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse("Erro de validação", HttpStatus.BAD_REQUEST, request.getRequestURI(), erros);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleOutrasExcecoes(Exception ex, HttpServletRequest request) {
        return buildErrorResponse("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), List.of(ex.getMessage()));
    }

    private ResponseEntity<ErroResponse> buildErrorResponse(String mensagem, HttpStatus status, String path, List<String> detalhes) {
        ErroResponse erro = new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                path,
                detalhes
        );
        return ResponseEntity.status(status).body(erro);
    }
}
