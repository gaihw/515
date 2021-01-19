package com.port.websocket.util;

import java.util.stream.Stream;

public enum HttpStatusEnum {

    OK(200, "success"),

    BAD_REQUEST(400, "Wrong request content, behavior, format"),

    UNAUTHORIZED(401, "Wrong authentication information"),

    NOT_FOUND(404, "Wrong request path"),

    PAYLOAD_TOO_LARGE(413, "Bulk request"),

    TOO_MANY_REQUESTS(429, "Access frequency exceeds limit"),

    VIOLATION_PUNISH(424, "Penalized for continuing access after the frequency exceeds the limit"),

    INTERNAL_SERVER_ERROR(500, "There is a problem with the server"),

    BAD_GATEWAY(502, "Business service problem");

    private final int code;

    private final String message;

    HttpStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static HttpStatusEnum of(int code) {
        return Stream.of(HttpStatusEnum.values()).filter(httpStatusEnum -> httpStatusEnum.getCode() == code).findFirst().orElse(INTERNAL_SERVER_ERROR);
    }
}
