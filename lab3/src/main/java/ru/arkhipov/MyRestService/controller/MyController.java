package ru.arkhipov.MyRestService.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MyRestService.exception.UnsupportedCodeException;
import ru.arkhipov.MyRestService.exception.ValidationFailedException;
import ru.arkhipov.MyRestService.model.Codes;
import ru.arkhipov.MyRestService.model.ErrorCodes;
import ru.arkhipov.MyRestService.model.ErrorMessages;
import ru.arkhipov.MyRestService.model.Request;
import ru.arkhipov.MyRestService.model.Response;
import ru.arkhipov.MyRestService.service.ModifyResponseService;
import ru.arkhipov.MyRestService.service.RequestService;
import ru.arkhipov.MyRestService.util.DateTimeUtil;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MyController {

    private final RequestService requestService;
    private final ModifyResponseService modifySystemTimeResponseService;
    private final ModifyResponseService modifyOperationUidResponseService;

    public MyController(RequestService requestService,
                        @Qualifier("modifySystemTimeResponseService") ModifyResponseService modifySystemTimeResponseService,
                        @Qualifier("modifyOperationUidResponseService") ModifyResponseService modifyOperationUidResponseService) {
        this.requestService = requestService;
        this.modifySystemTimeResponseService = modifySystemTimeResponseService;
        this.modifyOperationUidResponseService = modifyOperationUidResponseService;
    }

    @PostMapping("/process")
    public ResponseEntity<Response> process(@Valid @RequestBody Request request, BindingResult bindingResult) {
        log.info("Received request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Initial response created: {}", response);

        try {
            if (bindingResult.hasErrors()) {
                String details = bindingResult.getFieldErrors().stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .collect(Collectors.joining("; "));
                log.error("Validation failed for request {}: {}", request, details);
                throw new ValidationFailedException(details);
            }

            response = requestService.process(request);
            log.info("Response received from service: {}", response);
        } catch (ValidationFailedException e) {
            log.error("ValidationFailedException: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response changed after validation error: {}", response);
            return ResponseEntity.badRequest().body(response);
        } catch (UnsupportedCodeException e) {
            log.error("UnsupportedCodeException: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response changed after unsupported code error: {}", response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected exception: ", e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response changed after unknown error: {}", response);
            return ResponseEntity.internalServerError().body(response);
        }

        modifySystemTimeResponseService.modify(response);
        modifyOperationUidResponseService.modify(response);
        log.info("Final response: {}", response);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleUnreadableBody(HttpMessageNotReadableException e) {
        log.error("Request body cannot be read: {}", e.getMessage());
        Response response = Response.builder()
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.VALIDATION_EXCEPTION)
                .errorMessage(ErrorMessages.VALIDATION)
                .build();
        log.info("Response for unreadable body: {}", response);
        return ResponseEntity.badRequest().body(response);
    }
}
