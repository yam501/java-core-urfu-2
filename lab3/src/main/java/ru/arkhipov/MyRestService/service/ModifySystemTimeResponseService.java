package ru.arkhipov.MyRestService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MyRestService.model.Response;
import ru.arkhipov.MyRestService.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@Service
public class ModifySystemTimeResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        log.info("Response before systemTime modification: {}", response);
        response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));
        log.info("Response after systemTime modification: {}", response);
        return response;
    }
}
