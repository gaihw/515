package com.atom.dex.api.client.test.exception;

import lombok.Data;

@Data
public class AtomError {

    private String code;

    private String name;

    private String what;

    private AtomErrorDetails[] details;

    private AtomError(){

    }


}
