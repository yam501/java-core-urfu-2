package ru.arkhipov.MyRestService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MyRestService.exception.UnsupportedCodeException;
import ru.arkhipov.MyRestService.model.Codes;
import ru.arkhipov.MyRestService.model.ErrorCodes;
import ru.arkhipov.MyRestService.model.ErrorMessages;
import ru.arkhipov.MyRestService.model.Request;
import ru.arkhipov.MyRestService.model.Response;
import ru.arkhipov.MyRestService.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public Response process(Request request) {
        log.info("Processing request in service: {}", request);

        if ("123".equals(request.getUid())) {
            log.error("Unsupported uid: {}", request.getUid());
            throw new UnsupportedCodeException("Код uid=" + request.getUid() + " не поддерживается");
        }

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Response built in service: {}", response);
        return response;
    }
}
