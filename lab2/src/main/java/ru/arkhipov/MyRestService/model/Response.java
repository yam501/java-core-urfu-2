package ru.arkhipov.MyRestService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    private String code;
    private String errorCode;
    private String errorMessage;
    private String operationUid;
    private String systemTime;
    private String uid;
}
