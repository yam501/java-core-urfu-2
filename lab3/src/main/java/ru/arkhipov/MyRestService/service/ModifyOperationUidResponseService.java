package ru.arkhipov.MyRestService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MyRestService.model.Response;

import java.util.UUID;

@Slf4j
@Service
public class ModifyOperationUidResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        log.info("Response before operationUid modification: {}", response);
        response.setOperationUid(UUID.randomUUID().toString());
        log.info("Response after operationUid modification: {}", response);
        return response;
    }
}
