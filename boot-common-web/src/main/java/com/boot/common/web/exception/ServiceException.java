package com.boot.common.web.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class ServiceException extends RuntimeException{

    private final int code;

    private final String message;
}
