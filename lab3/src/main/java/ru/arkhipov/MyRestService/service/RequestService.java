package ru.arkhipov.MyRestService.service;

import ru.arkhipov.MyRestService.model.Request;
import ru.arkhipov.MyRestService.model.Response;

public interface RequestService {

    Response process(Request request);
}
