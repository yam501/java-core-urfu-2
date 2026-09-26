package ru.arkhipov.MyRestService.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MyRestService.exception.UnsupportedCodeException;
import ru.arkhipov.MyRestService.model.Request;
import ru.arkhipov.MyRestService.model.Response;

import java.time.Instant;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public Response process(Request request) {
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException("Код uid=" + request.getUid() + " не поддерживается");
        }

        return Response.builder()
                .code("success")
                .errorCode("")
                .errorMessage("")
                .operationUid(request.getOperationUid())
                .systemTime(Instant.now().toString())
                .uid(request.getUid())
                .build();
    }
}
