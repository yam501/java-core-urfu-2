package ru.arkhipov.MyRestService.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MyRestService.exception.UnsupportedCodeException;
import ru.arkhipov.MyRestService.exception.ValidationFailedException;
import ru.arkhipov.MyRestService.model.Request;
import ru.arkhipov.MyRestService.model.Response;
import ru.arkhipov.MyRestService.service.RequestService;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class MyController {

    private final RequestService requestService;

    public MyController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/process")
    public Response process(@Valid @RequestBody Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            throw new ValidationFailedException(message);
        }
        return requestService.process(request);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public Response handleValidationFailed(ValidationFailedException ex) {
        return Response.builder()
                .code("failed")
                .errorCode("ValidationException")
                .errorMessage(ex.getMessage())
                .systemTime(Instant.now().toString())
                .build();
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public Response handleUnsupportedCode(UnsupportedCodeException ex) {
        return Response.builder()
                .code("failed")
                .errorCode("UnsupportedCodeException")
                .errorMessage(ex.getMessage())
                .systemTime(Instant.now().toString())
                .build();
    }
}
