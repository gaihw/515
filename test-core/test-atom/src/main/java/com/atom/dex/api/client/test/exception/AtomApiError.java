package com.atom.dex.api.client.test.exception;


import lombok.Data;

@Data
public class AtomApiError {
    private String message;
    private String code;
    private AtomError error;

}
