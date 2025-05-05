package com.cltech.assistencia_tecnica.exception;

import com.cltech.assistencia_tecnica.exception.base.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<ErroResponse> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI(), null);
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroResponse> handleRequisicaoInvalida(RequisicaoInvalidaException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI(), null);
    }

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ResponseEntity<ErroResponse> handleConflitoDeDados(ConflitoDeDadosException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse("Erro de validação", HttpStatus.BAD_REQUEST, request.getRequestURI(), erros);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> detalhes = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        return buildErrorResponse("Violação de restrição", HttpStatus.BAD_REQUEST, request.getRequestURI(), detalhes);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleOutrasExcecoes(Exception ex, HttpServletRequest request) {
        return buildErrorResponse("Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleViolacaoDeIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse("Violação de integridade de dados", HttpStatus.CONFLICT, request.getRequestURI(), List.of(ex.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        ErroResponse erro = ErroResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro de validação")
                .mensagem(ex.getMessage())
                .path(request.getRequestURI())
                .detalhes(Collections.singletonList("Verifique os valores enviados"))
                .build();

        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErroResponse erro = ErroResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .erro("Erro interno")
                .mensagem(ex.getMessage())
                .path(request.getRequestURI())
                .detalhes(Collections.singletonList("Contate o suporte técnico"))
                .build();

        return ResponseEntity.internalServerError().body(erro);
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
